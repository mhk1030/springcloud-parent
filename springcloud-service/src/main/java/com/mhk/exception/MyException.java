package com.mhk.exception;

/**
 * @作者 孟慧康
 * @时间 2019/8/12 20:55
 *
 *运行时异常处理类
 */
public class MyException  extends  RuntimeException{

    public MyException(String message) {
        super(message);
    }
}
