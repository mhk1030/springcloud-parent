package com.sso.dao;

import com.mhk.pojo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:25
 */
public interface RoleDao extends JpaRepository<Role,Long> {


    @Query(value = "select br.* from base_user_role bur INNER JOIN base_role br on bur.roleId = br.id where bur.userId = ?1",nativeQuery = true)
    public Role forRoleByUserId(Long userId);

}
