package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "work_role")
public class WorkRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工作角色名称
     */
    @Column(name = "work_role_name")
    private String workRoleName;

    /**
     * 工作角色
     */
    @Column(name = "work_role_key")
    private String workRoleKey;

    /**
     * 标签
     */
    private String tag;

    private String comment;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}