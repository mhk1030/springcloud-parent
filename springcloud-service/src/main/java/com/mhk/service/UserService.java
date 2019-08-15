package com.mhk.service;

import com.github.pagehelper.PageInfo;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/13 19:15
 */

public interface UserService {

    public PageInfo<User> selAll(Integer cpage,Integer pageSize,String userName, String start, String end, String sex1);

    public void add(User user);

    public void update(User user);

    public User selByLoginName(String loginName);

    public void del(Long id);

    public List<Role> selRole();

    public void delRole(Long userId);

    public void addRole(Long roleId,Long userId);
}
