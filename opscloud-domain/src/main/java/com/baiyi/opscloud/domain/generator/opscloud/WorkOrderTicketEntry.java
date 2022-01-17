package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "work_order_ticket_entry")
public class WorkOrderTicketEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单票据ID
     */
    @Column(name = "work_order_ticket_id")
    private Integer workOrderTicketId;

    /**
     * 名称
     */
    private String name;

    /**
     * 数据源实例UUID
     */
    @Column(name = "instance_uuid")
    private String instanceUuid;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 状态
     */
    @Column(name = "entry_status")
    private Integer entryStatus;

    /**
     * 条目Key
     */
    @Column(name = "entry_key")
    private String entryKey;

    /**
     * 角色
     */
    private String role;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 内容
     */
    private String content;

    /**
     * 处理结果
     */
    private String result;
}