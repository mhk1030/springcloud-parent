package com.mhk.pojo.base;

import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;

/**
 * @作者 孟慧康
 * @时间 2019/8/5 15:01
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class BaseAuditable {

   @Column(name="id")
    @Id
    Long id;

    @LastModifiedDate
    @Column(name="updateTime")
    Date updateTime;

    @CreatedDate
    @Column(name="createTime")
    Date createTime;

    @Version
    @Column(name = "version")
    private Long version;


}
