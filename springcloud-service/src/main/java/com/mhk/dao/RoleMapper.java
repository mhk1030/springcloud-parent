package com.mhk.dao;

import com.mhk.pojo.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/8 23:24
 */
@Mapper
public interface RoleMapper {

    public List<Role> selAll(String roleName);

    public void add(Role role);

    public void update(Role role);

    public void del(Long id);
}
