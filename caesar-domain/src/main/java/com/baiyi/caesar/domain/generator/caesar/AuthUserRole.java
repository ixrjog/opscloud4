package com.baiyi.caesar.domain.generator.caesar;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "auth_user_role")
public class AuthUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

}