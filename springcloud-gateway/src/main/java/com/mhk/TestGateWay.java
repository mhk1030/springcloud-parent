package com.mhk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 11:46
 */
@SpringBootApplication
@RestController
public class TestGateWay {
    public static void main(String[] args) {
        SpringApplication.run(TestGateWay.class,args);
    }


    @RequestMapping("serverhealth")
    public String serverhealth(){
        System.out.println("serverhealth==========");
        return "ok";
    }
}
