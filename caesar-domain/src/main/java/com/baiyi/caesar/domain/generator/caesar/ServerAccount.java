package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "server_account")
public class ServerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务器名称
     */
    private String username;

    /**
     * 凭据id
     */
    @Column(name = "credential_id")
    private Integer credentialId;

    /**
     * 0普通账户/1管理员
     */
    @Column(name = "account_type")
    private Integer accountType;

    /**
     * 连接协议
     */
    private String protocol;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 说明
     */
    private String comment;
}