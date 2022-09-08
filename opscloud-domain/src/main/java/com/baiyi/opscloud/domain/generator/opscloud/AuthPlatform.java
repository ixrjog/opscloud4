package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "auth_platform")
public class AuthPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 平台名称
     */
    private String name;

    /**
     * 认证令牌
     */
    private String token;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "credential_id")
    private Integer credentialId;

    /**
     * 平台描述
     */
    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}