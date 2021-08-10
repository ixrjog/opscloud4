package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.annotation.Decrypt;
import com.baiyi.opscloud.domain.annotation.Encrypt;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Decrypt
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 前端框架用户UUID
     */
    private String uuid;

    @Encrypt
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 显示名称
     */
    @Column(name = "display_name")
    private String displayName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_login")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;

    private String wechat;

    /**
     * 手机
     */
    private String phone;

    @Column(name = "created_by")
    private String createdBy;

    /**
     * 数据源
     */
    private String source;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;
}