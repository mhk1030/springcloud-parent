package com.mhk.service;

import com.github.pagehelper.PageInfo;
import com.mhk.pojo.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/8 23:36
 */

public interface RoleService {

    public PageInfo<Role> selAll(Integer cpage, Integer pageSize, String roleName);

    public void add(Role role);

    public void update(Role role);

    public void del(long id);
}
