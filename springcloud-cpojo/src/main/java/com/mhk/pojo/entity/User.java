package com.mhk.pojo.entity;

import com.mhk.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 14:36
 */
@Data
@Entity
@Table(name = "base_user")
public class User  {

    @Column(name="id")
    @Id
    Long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "loginName")
    private String loginName;

    @Column(name = "password")
    private String password;

    @Column(name = "tel")
    private String tel;

    @Column(name = "sex")
    private int sex;

    @Column(name = "parentId")
    private Long parentId;

    @Column(name = "url")
    private String url;

    @Column(name = "createTime")
    private Date createTime;

    @Transient
    private List<Menu> menuList;

    @Transient
    private Role role;

    @Transient
    private Map<String,String> authmap;

}
