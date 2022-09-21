package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "work_item")
public class WorkItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工作角色id
     */
    @Column(name = "work_role_id")
    private Integer workRoleId;

    /**
     * 类目父关系
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 工作角色名
     */
    @Column(name = "work_item_name")
    private String workItemName;

    /**
     * 工作类型
     */
    @Column(name = "work_item_key")
    private String workItemKey;

    /**
     * 类目颜色，前端专用
     */
    private String color;

    private String comment;

    /**
     * 默认内容
     */
    private String content;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;
}