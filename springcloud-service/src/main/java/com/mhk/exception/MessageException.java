package com.mhk.exception;

import com.mhk.pojo.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @作者 孟慧康
 * @时间 2019/8/12 20:59
 */
@ControllerAdvice
public class MessageException {


    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ResponseResult myMessageException(Exception e){
        ResponseResult responseResult = ResponseResult.getResponseResult();
        responseResult.setCode(501);
        if(e instanceof  MyException){
            MyException myException = (MyException) e;
            responseResult.setSuccess(myException.getMessage());
        }
        responseResult.setError("运行时未知错误");
        return responseResult;
    }

}
