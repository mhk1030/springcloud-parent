package com.mhk.pojo;

import lombok.Data;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 14:22
 */
@Data
public class ResponseResult {

    //返回信息编码   0 失败  1 成功
    private int code;
    //错误信息
    private String error;
    //程序返回结果
    private Object result;
    //成功信息
    private Object success;
    //创建实例
    public static ResponseResult getResponseResult(){
        return new ResponseResult();
    }
    //登陆成功的标识(在这里存储一些用户的信息)
    private String token;
    //用来表示token的一个唯一的字符串
    private String tokenkey;
    //选中的需要回显的菜单ID
    private Long[] menuIds;


}
