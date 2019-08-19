package com.mhk.controller;

import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.Menu;
import com.mhk.service.MenuService;
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
 * @时间 2019/8/9 23:56
 */
@Api(tags = "权限管理")
@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("查询权限接口")
    @ApiOperation("这是查询权限的方法menuList")
    @RequestMapping("menuList")
    public ResponseResult seMenu(@RequestBody Map<String,String> map){
        List<Menu> list = menuService.selMenu(0L, 1,Long.valueOf(map.get("roleId")));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(list);
        responseResult.setCode(200);
        return responseResult;
    }

    @PostMapping("添加权限接口")
    @ApiOperation("这是权限添加的方法menuAdd")
    @RequestMapping("menuAdd")
    public ResponseResult add(@RequestBody Menu menu){
        menu.setLeval(menu.getLeval()+1);
        menu.setParentId(menu.getId());
        menu.setId(UID.next());
        System.out.println(menu);
        menuService.add(menu);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @PostMapping("修改权限接口")
    @ApiOperation("这是权限修改的方法menuUpdate")
    @RequestMapping("menuUpdate")
    public ResponseResult update(@RequestBody Menu menu){
        menuService.update(menu);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @PostMapping("删除权限接口")
    @ApiOperation("这是权限删除的方法menuDel")
    @RequestMapping("menuDel")
    public ResponseResult del(@RequestBody Menu menu){
        menuService.del(menu.getId());
        menuService.delByMenuId(menu.getId());
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }




}
