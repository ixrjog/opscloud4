package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "access_token")
@Builder
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 令牌标识
     */
    @Column(name = "token_id")
    private String tokenId;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 是否无效
     */
    @Builder.Default
    private Boolean valid = true;

    /**
     * 过期时间
     */
    @Column(name = "expired_time", updatable = false)
    private Date expiredTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 描述
     */
    private String comment;
}