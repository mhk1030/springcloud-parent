package com.mhk.controller;

import com.github.pagehelper.PageInfo;
import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.Role;
import com.mhk.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @作者 孟慧康
 * @时间 2019/8/8 23:41
 */
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("list")
    public ResponseResult selAll(@RequestBody Map<String,String> map){
        PageInfo<Role> pageInfo = roleService.selAll(Integer.valueOf(map.get("cpage")), Integer.valueOf(map.get("pageSize")), map.get("roleName"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(pageInfo);
        responseResult.setCode(200);
        return responseResult;
    }

    @RequestMapping("add")
    public ResponseResult add(@RequestBody Role role){
        roleService.add(role);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return  responseResult;
    }

    @RequestMapping("update")
    public ResponseResult update(@RequestBody Role role){
        roleService.update(role);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return  responseResult;
    }

    @RequestMapping("del")
    public ResponseResult del(@RequestBody Role role){
        roleService.del(role.getId());
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return  responseResult;
    }


}
