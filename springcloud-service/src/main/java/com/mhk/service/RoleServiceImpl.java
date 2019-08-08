package com.mhk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhk.dao.RoleMapper;
import com.mhk.pojo.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/8 23:37
 */
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public  PageInfo<Role> selAll(Integer cpage,Integer pageSize,String roleName) {
        PageHelper.startPage(cpage,pageSize);
        List<Role> list = roleMapper.selAll(roleName);
        PageInfo<Role> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public void add(Role role) {
        roleMapper.add(role);
    }

    @Override
    public void update(Role role) {
        roleMapper.update(role);
    }

    @Override
    public void del(Integer id) {
        roleMapper.del(id);
    }
}
