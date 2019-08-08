package com.mhk.test;

import com.mhk.dao.UserDao;
import com.mhk.pojo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @作者 孟慧康
 * @时间 2019/8/8 16:53
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestS {

    @Autowired
    private UserDao userDao;

    @Test
    public void add(){
        User user = new User();
        user.setId(111L);
        user.setUserName("ww");
        user.setLoginName("rr");
        user.setPassword("ddd");
        user.setSex(1);
        userDao.save(user);
    }
}
