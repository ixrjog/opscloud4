package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "i18n_en")
    private String i18nEn;

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

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}