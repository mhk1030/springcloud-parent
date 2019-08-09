package com.mhk.dao;

import com.mhk.pojo.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/9 23:50
 */
@Mapper
public interface MenuMapper {

    public List<Menu> selMenu(@Param("pid") Long pid, @Param("leval") Integer leval);
}
