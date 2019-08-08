package com.mhk.pojo.entity;

import com.mhk.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 14:43
 */
@Data
@Entity
@Table(name = "base_role")
public class Role extends BaseAuditable {

    @Column(name="id")
    @Id
    Long id;

    @Column(name = "roleName")
    private String roleName;

    @Column(name = "miaoshu")
    private String miaoshu;


}
