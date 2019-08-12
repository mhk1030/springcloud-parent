package com.mhk.service;

import com.mhk.dao.MenuMapper;
import com.mhk.pojo.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/9 23:53
 */
@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<Menu> selMenu(Long pid, Integer leval) {
        List<Menu> list = menuMapper.selMenu(pid, leval);
        this.getForMenu(list);
        return list;
    }

    public void getForMenu(List<Menu> firstMenu){
        for (Menu menu: firstMenu) {
            Long pid = menu.getId();
            int leval = menu.getLeval()+1;
            List<Menu> menus = menuMapper.selMenu(pid, leval);
            System.out.println(menus);
            if(menus != null){
                menu.setMenuList(menus);
                getForMenu(menus);
            }else{
                break;
            }
        }

    }


    @Override
    public void add(Menu menu) {
        menuMapper.add(menu);
    }

    @Override
    public void update(Menu menu) {
        menuMapper.update(menu);
    }

    @Override
    public void del(Long id) {
        menuMapper.del(id);
    }


    @Override
    public void delByMenuId(Long menuId) {
        menuMapper.delByMenuId(menuId);
    }


}
