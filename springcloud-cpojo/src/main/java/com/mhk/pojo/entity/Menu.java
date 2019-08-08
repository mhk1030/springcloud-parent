package com.mhk.pojo.entity;

import com.mhk.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 14:45
 */
@Entity
@Data
@Table(name = "base_menu")
public class Menu extends BaseAuditable {

    @Column(name="id")
    @Id
    Long id;

    @Column(name = "menuName")
    private String menuName;

    @Column(name = "parentId")
    private Long parentId;

    @Column(name = "leval")
    private int leval;

    @Column(name = "url")
    private String url;

    @Transient
    private List<Menu> menuList;


}
