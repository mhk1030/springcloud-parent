package com.mhk.service;

import com.mhk.pojo.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/9 23:52
 */
public interface MenuService {

    public List<Menu> selMenu( Long pid,Integer leval);
}
