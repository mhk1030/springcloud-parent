package com.mhk.service;

import com.github.pagehelper.PageInfo;
import com.mhk.pojo.entity.Menu;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @作者 孟慧康
 * @时间 2019/8/8 23:36
 */

public interface RoleService {

    public PageInfo<Role> selAll(Integer cpage, Integer pageSize, String roleName);

    public void add(Role role);

    public void update(Role role);

    public void del(Long id);

    public List<User> selByRoleId(Long roleId);

    public List<Menu> selMenu(Long pid,Integer leval,Long roleId);
}
