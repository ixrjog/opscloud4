package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "server_account")
@Builder
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

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 说明
     */
    private String comment;
}