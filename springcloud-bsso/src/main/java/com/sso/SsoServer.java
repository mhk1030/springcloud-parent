package com.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:15
 */
@SpringBootApplication
@EnableJpaAuditing
@RestController
@EntityScan(basePackages = {"com.mhk.pojo.**"})
public class SsoServer {

    public static void main(String[] args) {
        SpringApplication.run(SsoServer.class,args);
    }

    @RequestMapping(name = "health")
    public String health(){
        System.out.println("===SSO-SERVER==OK != ==========");
        return "ok";
    }

}
