package com.sso.controller;

import com.alibaba.fastjson.JSON;

import com.mhk.exception.LoginException;
import com.mhk.jwt.JWTUtils;
import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.User;
import com.mhk.random.VerifyCodeUtils;
import com.mhk.utils.MD5;
import com.mhk.utils.UID;
import com.sso.service.UserService;
import com.sso.utils.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:42
 */
@Api(tags = "这是sso单点登录接口")
@RestController
public class AuthController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserService userService;

    @PostMapping("登录接口")
    @ApiOperation("这是登录的方法login")
    @RequestMapping("login")
    public ResponseResult toLogin(@RequestBody Map<String,Object> map) throws LoginException, ParseException {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        //从redis中取出code
        String codekey = redisTemplate.opsForValue().get(map.get("codekey").toString());
        System.out.println("-------"+codekey);
        if (codekey == null || !codekey.equals(map.get("code").toString())) {
            responseResult.setCode(500);
            responseResult.setError("验证码错误");
            return responseResult;
        }
        if (map != null && map.get("loginname") != null) {
            User user = userService.getUserByLogin(map.get("loginname").toString());
            System.out.println("..........."+user);
            if (user != null) {
                String password = MD5.encryptPassword(map.get("password").toString(), "mhk");
                System.out.println(password);
                if (user.getPassword().equals(password)) {
                    User user1 = new User();
                    user1.setId(user.getId());
                    user1.setUserName(user.getUserName());
                    user1.setLoginName(user.getLoginName());
                    user1.setAuthmap(user.getAuthmap());
                    String userinfo = JSON.toJSONString(user1);
                    System.out.println(userinfo);
                    String token = JWTUtils.generateToken(userinfo);
                    responseResult.setToken(token);

                    redisTemplate.opsForValue().set("USERINFO" + user.getId(), token);

                    redisTemplate.opsForHash().putAll("USERDATAAUTH" + user.getId(), user.getAuthmap());

                    redisTemplate.expire("USERINFO" + user.getId().toString(), 1000*60*60*24*7, TimeUnit.SECONDS);



                    /*用户访问量统计*/
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String mouth = simpleDateFormat.format(new Date());

                    //redisTemplate.expire(simpleDateFormat.format(new Date()),1000*60*60*24,TimeUnit.MINUTES);

                    if(!redisTemplate.hasKey(mouth+user.getId())){
                        redisTemplate.opsForValue().set(mouth+user.getId(),"");
                        redisTemplate.expire(mouth+user.getId(),1000*60*60*60*24,TimeUnit.MINUTES);
                        if(redisTemplate.hasKey(mouth)){
                            String value = redisTemplate.opsForValue().get(mouth);
                            Integer value1 = Integer.valueOf(value)+1;
                            redisTemplate.opsForValue().set(mouth,value1.toString());
                        }else{
                            redisTemplate.opsForValue().set(mouth,"1");
                        }
                    }else{
                        redisTemplate.opsForValue().set(mouth+user.getId(),"");
                        redisTemplate.expire(mouth+user.getId(),1000*60*60*60*24,TimeUnit.MINUTES);
                    }
                    String[] values = new String[7];
                    String[] keys = new String[7];
                    String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                    for (int i = 0; i <keys.length ; i++) {
                        String str=null;
                        if(!redisTemplate.hasKey(format)){
                            str = "0";
                        }
                         str = redisTemplate.opsForValue().get(format);
                        values[i]=str;
                        keys[i]=format;
                        redisTemplate.expire(format,1000*60*60*24*(7-i),TimeUnit.MINUTES);
                        format=simpleDateFormat.format(new Date(System.currentTimeMillis()-1000*60*60*24*(i+1)));
                    }
                    /*用户访问量统计*/
                    user.setKeys(keys);
                    user.setValues(values);
                    responseResult.setResult(user);
                    responseResult.setCode(200);
                    return responseResult;
                } else {
                    throw new LoginException("用户名或密码错误");
                }

            } else {
                throw new LoginException("用户名或密码错误");
            }

        } else {
            throw new LoginException("用户名或密码错误");
        }

    }

    /**
     * 获取滑动验证的验证码
     * @param request
     * @param response
     * @return
     */
    @PostMapping("验证码接口")
    @ApiOperation("这是登录的方法获取验证码方法")
    @RequestMapping("getCode")
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        //生成一个长度是5的随机字符串
        String code = VerifyCodeUtils.generateVerifyCode(5);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(code);
        System.out.println(code);
        String uidCode = "CODE"+ UID.getUUID16();
        //将生成的随机字符串标识后存入redis
        redisTemplate.opsForValue().set(uidCode,code);
        //设置过期时间
        redisTemplate.expire(uidCode,1, TimeUnit.MINUTES);
        //回写cookie
        Cookie cookie = new Cookie("authcode", uidCode);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        return responseResult;
    }


    @PostMapping("手机验证接口")
    @ApiOperation("这是手机发送验证码的方法getcode")
   @RequestMapping("getcode")
    public ResponseResult getcode(@RequestBody Map<String,String> map){
       ResponseResult responseResult = ResponseResult.getResponseResult();
       if(map.get("phone")!=null){
           User user = userService.selByTel(map.get("phone"));

           if(user!=null){
               String code = HttpUtils.getCode();
               String host = "http://dingxin.market.alicloudapi.com";
               String path = "/dx/sendSms";
               String method = "POST";
               String appcode = "73179e47157746f68a96bec19108d297";
               Map<String, String> headers = new HashMap<String, String>();
               //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
               headers.put("Authorization", "APPCODE " + appcode);
               Map<String, String> querys = new HashMap<String, String>();
               querys.put("mobile", map.get("phone"));
               querys.put("param", "code:"+code);
               querys.put("tpl_id", "TP1711063");
               Map<String, String> bodys = new HashMap<String, String>();
               System.out.println(code+"llllllll");
               redisTemplate.opsForValue().set("code"+map.get("phone"),code);
               redisTemplate.expire("code"+map.get("phone"),60*1000,TimeUnit.MINUTES);
               responseResult.setCode(200);
               responseResult.setSuccess("发送成功");

               try {
                   HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
                   System.out.println(response.toString());
                   //获取response的body
                   //System.out.println(EntityUtils.toString(response.getEntity()));
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }else{
               responseResult.setCode(500);
               responseResult.setError("该用户不存在，请先注册");
           }

       }else{
           responseResult.setCode(500);
           responseResult.setError("请输入手机号");
       }
       return responseResult;
   }


    @PostMapping("手机验证码接口")
    @ApiOperation("这是判断验证码的方法smsLogin")
    @RequestMapping("smsLogin")
    public ResponseResult checkCode(@RequestBody Map<String,String> map){
        User user = userService.selByTel(map.get("phone"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String code = map.get("checkCode");
        if(code != null){
            String phone = redisTemplate.opsForValue().get("code"+map.get("phone"));
            redisTemplate.expire("code"+map.get("phone"),1000*60*60,TimeUnit.MINUTES);
            if(code.equals(phone)){

                User user1 = new User();
                user1.setId(user.getId());
                user1.setUserName(user.getUserName());
                user1.setLoginName(user.getLoginName());
                user1.setAuthmap(user.getAuthmap());
                String userinfo = JSON.toJSONString(user1);
                System.out.println(userinfo);
                String token = JWTUtils.generateToken(userinfo);
                responseResult.setToken(token);

                redisTemplate.opsForValue().set("USERINFO" + user.getId(), token);

                redisTemplate.opsForHash().putAll("USERDATAAUTH" + user.getId(), user.getAuthmap());

                redisTemplate.expire("USERINFO" + user.getId().toString(), 1000*60*60*24*7, TimeUnit.SECONDS);

                /*用户访问量统计*/
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String mouth = simpleDateFormat.format(new Date());

                //redisTemplate.expire(simpleDateFormat.format(new Date()),1000*60*60*24,TimeUnit.MINUTES);

                if(!redisTemplate.hasKey(mouth+user.getId())){
                    redisTemplate.opsForValue().set(mouth+user.getId(),"");
                    redisTemplate.expire(mouth+user.getId(),1000*60*60*60*24,TimeUnit.MINUTES);
                    if(redisTemplate.hasKey(mouth)){
                        String value = redisTemplate.opsForValue().get(mouth);
                        Integer value1 = Integer.valueOf(value)+1;
                        redisTemplate.opsForValue().set(mouth,value1.toString());
                    }else{
                        redisTemplate.opsForValue().set(mouth,"1");
                    }
                }else{
                    redisTemplate.opsForValue().set(mouth+user.getId(),"");
                    redisTemplate.expire(mouth+user.getId(),1000*60*60*60*24,TimeUnit.MINUTES);
                }
                String[] values = new String[7];
                String[] keys = new String[7];
                String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                for (int i = 0; i <keys.length ; i++) {
                    String str=null;
                    if(!redisTemplate.hasKey(format)){
                        str = "0";
                    }
                    str = redisTemplate.opsForValue().get(format);
                    values[i]=str;
                    keys[i]=format;
                    redisTemplate.expire(format,1000*60*60*24*(7-i),TimeUnit.MINUTES);
                    format=simpleDateFormat.format(new Date(System.currentTimeMillis()-1000*60*60*24*(i+1)));
                }
                /*用户访问量统计*/
                user.setKeys(keys);
                user.setValues(values);

                System.out.println(user);
                responseResult.setResult(user);
                responseResult.setCode(200);
                responseResult.setSuccess("验证码有效");
            }else{
                responseResult.setError("验证码失效");
                responseResult.setCode(500);
            }
        }else{
            responseResult.setError("验证码为空");
            responseResult.setCode(500);
        }
        return responseResult;
    }

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("发送邮件接口")
    @ApiOperation("这是发送邮件的方法checkEamil")
    @RequestMapping("checkEmail")
    public ResponseResult checkEmail(@RequestBody Map<String,String> map){
        User user = userService.findEmail(map.get("username"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(user!=null){
            if(user.getEmail().equals(map.get("emails"))){
                MimeMessage message=mailSender.createMimeMessage();
                try {
                    //true表示需要创建一个multipart message
                    MimeMessageHelper helper=new MimeMessageHelper(message,true);
                    helper.setFrom(from);
                    helper.setTo(map.get("emails"));
                    helper.setSubject("密码重置");
                    String code = HttpUtils.getCode();
                    redisTemplate.opsForValue().set(code,user.getUserName());
                    redisTemplate.expire(code,1000*60,TimeUnit.MINUTES);
                    helper.setText("<html><head></head><body><a href="+"http://localhost:8080/email?code="+code+">http://localhost:8080/email?code="+code+"</a></body></html>",true);
                    mailSender.send(message);
                    System.out.println("html格式邮件发送成功");
                    responseResult.setCode(200);
                    responseResult.setSuccess("发送成功！");
                }catch (Exception e){
                    System.out.println("html格式邮件发送失败");
                }
            }else{
                responseResult.setCode(500);
                responseResult.setError("请输入正确邮箱！");
            }
        }else{
            responseResult.setCode(500);
            responseResult.setError("该用户不存在！");
        }
        return responseResult;
    }
    @PostMapping("邮箱更改密码接口")
    @ApiOperation("这是更改密码的方法editPass")
    @RequestMapping("editPass")
    public ResponseResult editPass(@RequestBody Map<String,String> map){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        String code = redisTemplate.opsForValue().get(map.get("code"));
        if(code != null){
            if(map.get("username").equals(code)){
                String password = MD5.encryptPassword(map.get("password"), "mhk");
                userService.editPass(password,map.get("username"));
                responseResult.setCode(200);
                responseResult.setSuccess("修改成功！");
            }else{
                responseResult.setCode(500);
                responseResult.setError("您的用户名与您所需要修改的用户不一致！");
            }
        }else{
            responseResult.setCode(500);
            responseResult.setError("请进行邮箱验证");
        }
        return responseResult;
    }




}
