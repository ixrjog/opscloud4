package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "instance_uuid")
    private String instanceUuid;

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

    private String comment;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

}