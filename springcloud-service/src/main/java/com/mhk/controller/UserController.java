package com.mhk.controller;

import com.mhk.dao.MenuDao;
import com.mhk.dao.RoleDao;
import com.mhk.dao.UserDao;
import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.User;

import com.mhk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                , map.get("start"), map.get("end"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(map1);
        responseResult.setCode(200);
        return responseResult;
    }


}
