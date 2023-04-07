package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.annotation.Decrypt;
import com.baiyi.opscloud.domain.annotation.Encrypt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Decrypt
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 7775998575519732616L;
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

    //@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_login")
    private Date lastLogin;

    private String wechat;

    /**
     * 手机
     */
    private String phone;

    /**
     * 启用MFA
     */
    private Boolean mfa;

    /**
     * 强制启用MFA
     */
    @Column(name = "force_mfa")
    private Boolean forceMfa;

    @Column(name = "created_by")
    private String createdBy;

    /**
     * 数据源
     */
    private String source;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;
}