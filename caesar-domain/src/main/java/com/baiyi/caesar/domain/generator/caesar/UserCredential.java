package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "user_credential")
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    /**
     * 有效
     */
    private Boolean valid;

    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户凭据分类
     */
    @Column(name = "credential_type")
    private Integer credentialType;

    /**
     * 凭据内容
     */
    private String credential;

    /**
     * 有效期
     */
    @Column(name = "expired_time")
    private Date expiredTime;

    private String fingerprint;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "create_time")
    private Date createTime;

    private String comment;
}