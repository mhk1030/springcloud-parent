package com.mhk.controller;

import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.Menu;
import com.mhk.service.MenuService;
import com.mhk.utils.UID;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("menuList")
    public ResponseResult seMenu(@RequestBody Map<String,String> map){
        List<Menu> list = menuService.selMenu(0L, 1,Long.valueOf(map.get("roleId")));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(list);
        responseResult.setCode(200);
        return responseResult;
    }

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

    @RequestMapping("menuUpdate")
    public ResponseResult update(@RequestBody Menu menu){
        menuService.update(menu);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @RequestMapping("menuDel")
    public ResponseResult del(@RequestBody Menu menu){
        menuService.del(menu.getId());
        menuService.delByMenuId(menu.getId());
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }




}
