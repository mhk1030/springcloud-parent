package com.mhk.controller;

import com.mhk.dao.MenuDao;
import com.mhk.dao.RoleDao;
import com.mhk.dao.UserDao;
import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;

import com.mhk.service.UserService;
import com.mhk.utils.MD5;
import com.mhk.utils.UID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @作者 孟慧康
 * @时间 2019/8/6 19:39
 */

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Autowired
   private UserService userService;

    @Autowired
    private UserDao userDao;


    private String url;

    private Integer status = 0;


    @RequestMapping("loginout")
    public ResponseResult loginout(@RequestBody Long id){
            redisTemplate.delete("USERINFO"+id);
            redisTemplate.delete("USERDATAAUTH"+id);

        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess("ok");
        return responseResult;
    }

    @RequestMapping("list")
    public ResponseResult selAll(@RequestBody Map<String,String> map){
        Map<String, Object> map1 = userService.selAll(Integer.valueOf(map.get("cpage").toString()), Integer.valueOf(map.get("pageSize").toString()), map.get("uname")
                , map.get("start"), map.get("end"),map.get("sex"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(map1);
        responseResult.setCode(200);
        return responseResult;
    }

    @RequestMapping("upload")
    public void upload(@Param("file")MultipartFile file) throws IOException {
        file.transferTo(new File("D:\\img\\"+file.getOriginalFilename()));
        url=file.getOriginalFilename();
        status = 1;

    }


    @RequestMapping("add")
    public ResponseResult add(@RequestBody User user){
        ResponseResult responseResult = ResponseResult.getResponseResult();

        User user1 = userService.selByLoginName(user.getLoginName());
        responseResult.setCode(500);
        responseResult.setError("此用户已存在！");
        if(user1 == null){
            user.setId(UID.next());
            user.setUrl(url);
            String password = MD5.encryptPassword(user.getPassword(), "mhk");
            user.setPassword(password);
            userService.add(user);
            responseResult.setError("");
            responseResult.setCode(200);
            responseResult.setSuccess("添加用户成功！");
        }

        return responseResult;
    }


    @RequestMapping("del")
    public ResponseResult del(@RequestBody User user){
        userService.del(user.getId());
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @RequestMapping("update")
    public ResponseResult update(@RequestBody User user) throws IllegalArgumentException{
        System.out.println(user);
        if(status == 1){
            user.setUrl(url);
            status = 0;
        }
        String password = MD5.encryptPassword(user.getPassword(), "mhk");
        user.setPassword(password);
            userDao.save(user);

        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @RequestMapping("selRole")
    public ResponseResult selRole(){
        List<Role> list = userService.selRole();
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(list);
        responseResult.setCode(200);
        return responseResult;
    }

    @RequestMapping("editRole")
    public ResponseResult editRole(@RequestBody Map<String,String> map){
        userService.delRole(Long.valueOf(map.get("userId").toString()));

        userService.addRole(Long.valueOf(map.get("roleId")),Long.valueOf(map.get("userId")));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }













}
