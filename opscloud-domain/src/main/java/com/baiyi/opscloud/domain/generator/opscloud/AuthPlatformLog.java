package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auth_platform_log")
public class AuthPlatformLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "platform_id")
    private Integer platformId;

    /**
     * 平台名称
     */
    @Column(name = "platform_name")
    private String platformName;

    /**
     * 认证令牌
     */
    private String username;

    /**
     * otp标识
     */
    private Boolean otp;

    /**
     * 认证结果
     */
    private Boolean result;

    /**
     * 认证消息
     */
    @Column(name = "result_msg")
    private String resultMsg;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}