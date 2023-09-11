package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "work_order")
public class WorkOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = -20798480896238199L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单名称
     */
    private String name;

    @Column(name = "i18n_en")
    private String i18nEn;

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
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    private String color;

    /**
     * 文档地址
     */
    private String docs;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 工作流配置
     */
    private String workflow;
}