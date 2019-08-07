package com.sso.dao;

import com.mhk.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:21
 */
public interface UserDao extends JpaRepository<User,Long> {

   
    public User findByLoginName(String loginName);
}
