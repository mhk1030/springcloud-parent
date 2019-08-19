package com.mhk.controller;

import com.github.pagehelper.PageInfo;
import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.Menu;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import com.mhk.service.RoleService;
import com.mhk.utils.UID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @作者 孟慧康
 * @时间 2019/8/8 23:41
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("角色查询接口")
    @ApiOperation("这是角色查询的方法roleList")
    @RequestMapping("roleList")
    public ResponseResult selAll(@RequestBody Map<String,String> map){
        PageInfo<Role> pageInfo = roleService.selAll(Integer.valueOf(map.get("cpage")), Integer.valueOf(map.get("pageSize")), map.get("roleName"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(pageInfo);
        responseResult.setCode(200);
        return responseResult;
    }

    @PostMapping("添加角色接口")
    @ApiOperation("这是角色添加的方法roleAdd")
    @RequestMapping("roleAdd")
    public ResponseResult add(@RequestBody Role role){
        role.setId(UID.next());
        Integer leval = role.getLeval();
        role.setLeval(leval+1);
        System.out.println(role);
        roleService.add(role);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return  responseResult;
    }

    @PostMapping("修改角色信息接口")
    @ApiOperation("这是修改角色的方法roleUpdate")
    @RequestMapping("roleUpdate")
    public ResponseResult update(@RequestBody Role role){
        roleService.update(role);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return  responseResult;
    }

    @PostMapping("删除角色接口")
    @ApiOperation("这是角色删除的方法roleDel")
    @RequestMapping("roleDel")
    public ResponseResult del(@RequestBody Role role){
        List<User> list = roleService.selByRoleId(role.getId());
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(500);
        responseResult.setError("此角色无用户绑定");
        if( list.size() != 0){
            role.setId(1908141057450001L);
            roleService.update(role);
            responseResult.setCode(200);
            responseResult.setError("");
            responseResult.setSuccess("解绑成功！");
        }
        return  responseResult;
    }

    @PostMapping("查询角色权限接口")
    @ApiOperation("这是绑定角色权限的方法power")
    @RequestMapping("power")
    public ResponseResult power(@RequestBody Map<String,String> map){
        List<Menu> list = roleService.selMenu(0L, 1,Long.valueOf(map.get("roleId")));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(list);
        responseResult.setCode(200);
        return responseResult;
    }


}
