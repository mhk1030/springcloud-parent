package com.mhk.dao;

import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/13 16:41
 */
@Mapper
public interface UserMapper {

    public List<User> selAll(@Param("userName") String userName,@Param("start") String start,@Param("end") String end,@Param("sex") String sex1);

    public Role selRoles(@Param("userId") Long userId);

    public void add(User user);

    public void update(User user);

    public User selByLoginName(@Param("loginName") String loginName);

    public void del(Long id);

    public List<Role> selRole();

    public void delRole(Long userId);

    public void addRole(@Param("roleId") Long roleId,@Param("userId") Long userId);

}
