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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:42
 */
@RestController
public class AuthController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserService userService;

    @RequestMapping("login")
    public ResponseResult toLogin(@RequestBody Map<String,Object> map) throws LoginException {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        //从redis中取出code
        String codekey = redisTemplate.opsForValue().get(map.get("codekey")).toString();
        System.out.println("-------"+codekey);
        if (codekey == null || !codekey.equals(map.get("code").toString())) {
            responseResult.setCode(500);
            responseResult.setError("验证码错误");
            return responseResult;
        }
        if (map != null && map.get("loginname") != null) {
            User user = userService.getUserByLogin(map.get("loginname").toString());
            if (user != null) {
                String password = MD5.encryptPassword(map.get("password").toString(), "mhk");
                if (user.getPassword().equals(password)) {
                    String userinfo = JSON.toJSONString(user);
                    String token = JWTUtils.generateToken(userinfo);
                    responseResult.setToken(token);

                    redisTemplate.opsForValue().set("USERINFO" + user.getId().toString(), token);

                    redisTemplate.opsForHash().putAll("USERDATAAUTH" + user.getId().toString(), user.getAuthmap());

                    redisTemplate.expire("USERINFO" + user.getId().toString(), 600, TimeUnit.SECONDS);

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




}
