package com.sso.dao;

import com.mhk.pojo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:30
 */
public interface MenuDao extends JpaRepository<Menu,Long> {

    @Query(value = "select bm.* from base_role_menu brm INNER JOIN base_menu bm on brm.menuId=bm.id where brm.roleId=?1 and bm.leval = ?2",nativeQuery = true)
    public List<Menu> getFirstMneu(Long roleId,Integer leval);



}
