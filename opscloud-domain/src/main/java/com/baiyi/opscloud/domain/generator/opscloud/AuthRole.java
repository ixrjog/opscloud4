package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "auth_role")
public class AuthRole implements IAllowOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 访问级别
     */
    @Column(name = "access_level")
    private Integer accessLevel;

    /**
     * 角色描述
     */
    private String comment;

    /**
     * 允许工作流申请
     */
    @Column(name = "allow_order")
    private Boolean allowOrder;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

}