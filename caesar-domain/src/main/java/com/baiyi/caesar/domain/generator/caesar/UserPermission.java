package com.baiyi.caesar.domain.generator.caesar;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "user_permission")
public class UserPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 角色
     */
    @Column(name = "permission_role")
    private String permissionRole;

    /**
     * 等级
     */
    private Integer rate;

    private String content;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}