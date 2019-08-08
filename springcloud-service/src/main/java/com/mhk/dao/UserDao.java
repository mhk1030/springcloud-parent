package com.mhk.dao;

import com.mhk.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:21
 */
public interface UserDao extends JpaRepository<User,Long> {


    @Transactional
    @Modifying
    @Query(value = "delete from base_user_role where userId=?1",nativeQuery = true)
    public void delRole(Long userId);

    @Transactional
    @Modifying
    @Query(value = "insert into base_user_role(roleId,userId) values(?1,?2)",nativeQuery = true)
    public void addRole(long roleId,long userId);

}
