package com.mhk.controller;

import com.github.pagehelper.PageInfo;
import com.mhk.pojo.ResponseResult;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;

import com.mhk.service.UserService;
import com.mhk.utils.MD5;
import com.mhk.utils.UID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @作者 孟慧康
 * @时间 2019/8/6 19:39
 */

@Api(tags = "用户管理")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Autowired
   private UserService userService;



    private String url;

    private Integer status = 0;

    @PostMapping("退出接口")
    @ApiOperation("这是退出的方法loginout")
    @RequestMapping("loginout")
    public ResponseResult loginout(@RequestBody Map<String,String> map){
            redisTemplate.delete("USERINFO"+Long.valueOf(map.get("id")));
            redisTemplate.delete("USERDATAAUTH"+Long.valueOf(map.get("id")));

        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        responseResult.setSuccess("退出成功");
        return responseResult;
    }
    @PostMapping("用户查询接口")
    @ApiOperation("这是查询用户的方法list")
    @RequestMapping("list")
    public ResponseResult selAll(@RequestBody Map<String,String> map){
        PageInfo<User> pageInfo = userService.selAll(Integer.valueOf(map.get("cpage").toString()), Integer.valueOf(map.get("pageSize").toString()), map.get("uname")
                , map.get("start"), map.get("end"), map.get("sex"));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(pageInfo);
        responseResult.setCode(200);
        return responseResult;
    }

    @PostMapping("上传接口")
    @ApiOperation("这是上传的方法upload")
    @RequestMapping("upload")
    public void upload(@Param("file")MultipartFile file) throws IOException {
        file.transferTo(new File("D:\\img\\"+file.getOriginalFilename()));
        url=file.getOriginalFilename();
        status = 1;

    }

    @PostMapping("添加接口")
    @ApiOperation("这是添加的方法add")
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
            userService.addRole(1908141057450001L,user.getId());
            responseResult.setError("");
            responseResult.setCode(200);
            responseResult.setSuccess("添加用户成功！");
        }

        return responseResult;
    }

    @PostMapping("用户删除接口")
    @ApiOperation("这是删除的方法del")
    @RequestMapping("del")
    public ResponseResult del(@RequestBody User user){
        userService.del(user.getId());
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @PostMapping("用户修改接口")
    @ApiOperation("这是修改的方法update")
    @RequestMapping("update")
    public ResponseResult update(@RequestBody User user) throws IllegalArgumentException{
        System.out.println(user);
        if(status == 1){
            user.setUrl(url);
            status = 0;
        }
        String password = MD5.encryptPassword(user.getPassword(), "mhk");
        user.setPassword(password);
        userService.update(user);

        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        responseResult.setSuccess("修改用户成功！");
        return responseResult;
    }

    @PostMapping("查找角色接口")
    @ApiOperation("这是查找角色的方法selRole")
    @RequestMapping("selRole")
    public ResponseResult selRole(){
        List<Role> list = userService.selRole();
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setResult(list);
        responseResult.setCode(200);

        return responseResult;
    }

    @PostMapping("修改绑定角色接口")
    @ApiOperation("这是修改绑定角色的方法editRole")
    @RequestMapping("editRole")
    public ResponseResult editRole(@RequestBody Map<String,String> map){
        userService.delRole(Long.valueOf(map.get("userId").toString()));

        userService.addRole(Long.valueOf(map.get("roleId")),Long.valueOf(map.get("userId")));
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }



    @RequestMapping("addExcel")
    @ApiOperation("导入excel数据进行批量添加")
    public ResponseResult addExcel(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        //获取传来的excel的输入流
        InputStream inputStream = multipartFile.getInputStream();

        //获得一个XSSFWorkbook的对象
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        //确认数据在哪个工作空间
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        int physicalNumberOfRows = sheetAt.getPhysicalNumberOfRows();

        for (int i = 1; i <physicalNumberOfRows ; i++) {
            XSSFRow row = sheetAt.getRow(i);
            User user = new User();
            user.setId(UID.next());
            user.setUserName(row.getCell(0).getStringCellValue());
            user.setLoginName(row.getCell(1).getStringCellValue());
            String password = row.getCell(2).getStringCellValue();
            String password1 = MD5.encryptPassword(password,"mhk");
            user.setPassword(password1);
            user.setSex((int)row.getCell(3).getNumericCellValue());
            user.setUrl(row.getCell(4).getStringCellValue());
            user.setTel(row.getCell(5).getStringCellValue());

            userService.add(user);
            userService.addRole(1908141057450001L,user.getId());
        }

        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        responseResult.setSuccess("导入成功");
        return responseResult;

    }










}
