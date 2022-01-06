package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "work_order_group")
public class WorkOrderGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单组名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 工单组类型
     */
    @Column(name = "group_type")
    private Integer groupType;

    /**
     * 图标
     */
    private String icon;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}