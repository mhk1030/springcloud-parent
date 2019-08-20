package com.mhk.pojo.entity;

import com.mhk.pojo.base.BaseAuditable;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @Transient
    private Long userid;

    @Column(name = "miaoshu")
    private String miaoshu;

    @Column(name = "leval")
    private Integer leval;

    @Transient
    private List<Long> menuIds;

    @Transient
    private Long[] menuId;

    @Transient
    private String userName;

    @Transient
    private List<Long> menuAll;

}
