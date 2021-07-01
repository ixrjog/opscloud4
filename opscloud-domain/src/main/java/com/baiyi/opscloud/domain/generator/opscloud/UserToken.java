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
@Table(name = "user_token")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 登录唯一标识
     */
    private String token;

    /**
     * 是否无效。0：无效；1：有效
     */
    private Boolean valid;

    @Column(name = "create_time" ,insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time" ,insertable = false, updatable = false)
    private Date updateTime;
}