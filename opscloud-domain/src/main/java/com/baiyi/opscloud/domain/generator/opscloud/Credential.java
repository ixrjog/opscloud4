package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "sys_credential")
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    /**
     * 凭据分类
     */
    private Integer kind;

    /**
     * 用户名
     */
    private String username;

    /**
     * 指纹
     */
    private String fingerprint;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 凭据内容
     */
    private String credential;

    /**
     * publicKey
     */
    @Column(name = "credential_2")
    private String credential2;

    /**
     * 密码短语
     */
    private String passphrase;

    private String comment;
}