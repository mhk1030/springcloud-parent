package com.sso.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;

/**
 * @作者 孟慧康
 * @时间 2019/8/16 23:07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test2 {

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void test04() throws FileNotFoundException {

        MimeMessage message=mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo("598489374@qq.com");
            helper.setSubject("密码重置");
            helper.setText("<html><head></head><body><a href='http://localhost:8080'/>http://localhost:8080</body></html>",true);
            mailSender.send(message);
            System.out.println("html格式邮件发送成功");
        }catch (Exception e){
            System.out.println("html格式邮件发送失败");
        }
    }



}

