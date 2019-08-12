package com.mhk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhk.dao.RoleMapper;
import com.mhk.pojo.entity.Menu;
import com.mhk.pojo.entity.Role;
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
    public String selByRoleId(Long roleId) {
        String userName = roleMapper.selByRoleId(roleId);
        return userName;
    }

    @Override
    public List<Menu> selMenu(Long pid, Integer leval) {
        List<Menu> list = roleMapper.selMenu(pid, leval);
        System.out.println("=============");
        this.getForMenu(list);
        return list;
    }

    public void getForMenu(List<Menu> firstMenu){
        for (Menu menu: firstMenu) {
            Long pid = menu.getId();
            int leval = menu.getLeval()+1;
            List<Menu> menus = roleMapper.selMenu(pid, leval);
            System.out.println(menus);
            if(menus != null){
                menu.setMenuList(menus);
                getForMenu(menus);
            }else{
                break;
            }
        }

    }
}
