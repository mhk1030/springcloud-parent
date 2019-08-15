package com.mhk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhk.dao.RoleMapper;
import com.mhk.pojo.entity.Menu;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        for (Role role:list) {
            List<Long> list1 = roleMapper.selMenuByRoleId(role.getId());
            role.setMenuIds(list1);
        }
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
        roleMapper.delRoleMenu(role.getId());
        if(role.getMenuId().length != 0 ){
            roleMapper.addMenu(role.getId(),role.getMenuId());
        }
    }

    @Override
    public void del(Long id) {
        roleMapper.del(id);
        roleMapper.delRoleMenu(id);
        roleMapper.delUserRole(id);
    }

    @Override
    public List<User> selByRoleId(Long roleId) {
        List<User> list = roleMapper.selByRoleId(roleId);
        return list;
    }

    @Override
    public List<Menu> selMenu(Long pid, Integer leval,Long roleId) {
        List<Menu> list = roleMapper.selMenu(pid, leval,roleId);
        System.out.println("=============");
        this.getForMenu(list,roleId);
        return list;
    }

    public void getForMenu(List<Menu> firstMenu,Long roleId){
        for (Menu menu: firstMenu) {
            Long pid = menu.getId();
            int leval = menu.getLeval()+1;
            List<Menu> menus = roleMapper.selMenu(pid, leval,roleId);
            System.out.println(menus);
            if(menus != null){
                menu.setMenuList(menus);
                getForMenu(menus,roleId);
            }else{
                break;
            }
        }

    }
}
