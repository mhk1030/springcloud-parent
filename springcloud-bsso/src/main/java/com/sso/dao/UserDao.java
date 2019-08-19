package com.sso.dao;

import com.mhk.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:21
 */
public interface UserDao extends JpaRepository<User,Long> {


    public User findByLoginName(String loginName);

    public User findByTel(String tel);

    public User findByUserName(String userName);

    @Modifying
    @Transactional
    @Query(value = "update base_user set password=?1 where userName=?2",nativeQuery = true)
    public void  editPass(String password,String userName);
}
