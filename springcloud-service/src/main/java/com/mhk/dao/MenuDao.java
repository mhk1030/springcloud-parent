package com.mhk.dao;

import com.mhk.pojo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:30
 */
public interface MenuDao extends JpaRepository<Menu,Long> {


}
