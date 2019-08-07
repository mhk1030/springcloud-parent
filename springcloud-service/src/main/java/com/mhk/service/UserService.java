package com.mhk.service;

import com.mhk.dao.RoleDao;
import com.mhk.dao.UserDao;
import com.mhk.pojo.entity.Role;
import com.mhk.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者 孟慧康
 * @时间 2019/8/6 20:10
 */
@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    public  Map<String, Object> selAll(Integer cpage,Integer pageSize,String uname,String start,String end){
        EntityManager entityManager = localContainerEntityManagerFactoryBean.getNativeEntityManagerFactory().createEntityManager();

        StringBuffer stringBuffer = new StringBuffer("select * from base_user where 1=1");
        StringBuffer stringBuffer1 = new StringBuffer("select count(*) from base_user where 1=1");

        if( uname !=null && uname != ""){
            stringBuffer.append(" and  userName like concat('%','"+uname+"','%')");
            stringBuffer1.append(" and  userName like concat('%','"+uname+"','%') ");
        }
        if(start != null && start != ""){
            stringBuffer.append(" and createTime >= '"+start+"'");
            stringBuffer1.append(" and createTime >= '"+start+"'");
        }
        if(end != null && end != ""){
            stringBuffer.append(" and createTime <= '"+end+"'");
            stringBuffer1.append(" and createTime <= '"+end+"'");
        }
        /*if(sex != null){
            stringBuffer.append(" and sex = "+sex+"");
            stringBuffer1.append(" and sex = "+sex+"");
        }*/

        stringBuffer.append(" limit "+(cpage-1)*pageSize+","+pageSize);

        Query nativeQuery = entityManager.createNativeQuery(stringBuffer.toString(), User.class);
        Query nativeQuery1 = entityManager.createNativeQuery(stringBuffer1.toString());

        List<User> list = nativeQuery.getResultList();
        for (User user: list) {
            Role role = roleDao.selById(user.getId());
            user.setRole(role);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("total",nativeQuery1.getResultList().get(0));
        map.put("list",list);
        return map;
    }


}
