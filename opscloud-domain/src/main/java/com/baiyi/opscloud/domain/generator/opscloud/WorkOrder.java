package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "work_order")
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 工单key
     */
    @Column(name = "work_order_key")
    private String workOrderKey;

    /**
     * 帮助文档id
     */
    @Column(name = "sys_document_id")
    private Integer sysDocumentId;

    /**
     * 工单组id
     */
    @Column(name = "work_order_group_id")
    private Integer workOrderGroupId;

    /**
     * 状态 0 正常 1 开发 2 停用
     */
    private Integer status;

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

    /**
     * 工作流配置
     */
    private String workflow;
}