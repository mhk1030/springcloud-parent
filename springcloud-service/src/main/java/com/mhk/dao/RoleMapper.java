package com.mhk.dao;

import com.mhk.pojo.entity.Menu;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/8 23:24
 */
@Mapper
public interface RoleMapper {

    public List<Role> selAll(@Param("roleName") String roleName);

    public List<Long> selMenuByRoleId(@Param("roleId") Long roleId);

    public List<Long> selMenuAll(@Param("roleId") Long roleId);

    public void add(Role role);

    public void update(Role role);

    public void addMenu(@Param("roleId")Long roleId,@Param("menuId") Long[] menuId);

    public void addUser(@Param("roleId") Long roleId,@Param("userId") Long userId);

    public void del(Long id);

    public List<User> selByRoleId(@Param("roleId") Long roleId);

    public void delRoleMenu(@Param("roleId") Long roleId);

    public  void  delUserRole(@Param("roleId") Long roleId);

    public List<Menu> selMenu(@Param("pid") Long pid,@Param("leval") Integer leval,@Param("roleId") Long roleId);
}
