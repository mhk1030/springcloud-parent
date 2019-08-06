package com.mhk.controller;

import com.mhk.dao.MenuDao;
import com.mhk.dao.RoleDao;
import com.mhk.dao.UserDao;
import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者 孟慧康
 * @时间 2019/8/6 19:39
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping("getUser")
    public ResponseResult getUser(@RequestBody Long id){
        User user = userDao.findById(id).get();
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(user);
        responseResult.setCode(200);
        return responseResult;
    }


}
