package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "auth_resource")
public class AuthResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源组id
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 资源名称
     */
    @Column(name = "resource_name")
    private String resourceName;

    private String comment;

    /**
     * 是否鉴权
     */
    @Column(name = "need_auth")
    private Boolean needAuth;

    /**
     * 用户界面
     */
    private Boolean ui;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

}