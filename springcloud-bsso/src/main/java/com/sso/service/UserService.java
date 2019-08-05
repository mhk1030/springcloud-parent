package com.sso.service;

import com.mhk.pojo.entity.Menu;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import com.sso.dao.MenuDao;
import com.sso.dao.RoleDao;
import com.sso.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:53
 */
@Component
public class UserService  {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MenuDao menuDao;


    public User getUserByLogin(String loginName){
        User byLoginName = userDao.findByLoginName(loginName);
        if(byLoginName != null){
            Role role = roleDao.forRoleByUserId(byLoginName.getId());
            byLoginName.setRole(role);
            if(role != null){
                List<Menu> firstMneu = menuDao.getFirstMneu(role.getId(), 1);
                Map<String, String> authMap = new Hashtable<>();
                this.getForMenu();
                byLoginName.setAuthmap(authMap);
                byLoginName.setMenuList(firstMneu);
            }

        }
        return byLoginName;

    }

    private void getForMenu() {
    }


}
