package com.mhk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhk.dao.UserMapper;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/13 19:18
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<User> selAll(Integer cpage,Integer pageSize,String userName, String start, String end, String sex1) {
        PageHelper.startPage(cpage,pageSize);
        List<User> list = userMapper.selAll(userName, start, end, sex1);
        for (User user: list) {
            Role role = userMapper.selRoles(user.getId());
            user.setRole(role);
        }
        PageInfo<User> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public void add(User user) {
        userMapper.add(user);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public User selByLoginName(String loginName) {
        User user = userMapper.selByLoginName(loginName);
        return user;
    }

    @Override
    public void del(Long id) {
        userMapper.del(id);
    }


    @Override
    public List<Role> selRole() {
        List<Role> list = userMapper.selRole();
        return list;
    }


    @Override
    public void delRole(Long userId) {
            userMapper.delRole(userId);

    }

    @Override
    public void addRole(Long roleId, Long userId) {
        userMapper.addRole(roleId,userId);
    }
}
