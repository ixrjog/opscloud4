package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "user_group")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 组名
     */
    private String name;

    /**
     * 组类型
     */
    @Column(name = "group_type")
    private Integer groupType;

    /**
     * 数据源
     */
    private String source;

    /**
     * 允许工单申请
     */
    @Column(name = "allow_workorder")
    private Boolean allowWorkorder;

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

    /**
     * 描述
     */
    private String comment;
}