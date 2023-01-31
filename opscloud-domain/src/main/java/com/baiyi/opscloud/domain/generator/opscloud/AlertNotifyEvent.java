package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Table(name = "alert_notify_event")
public class AlertNotifyEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 告警uuid
     */
    @Column(name = "event_uuid")
    private String eventUuid;

    /**
     * 告警名称
     */
    @Column(name = "alert_name")
    private String alertName;

    /**
     * 告警等级  MAX、MID、LOW
     */
    private String severity;

    /**
     * 告警描述
     */
    private String message;

    /**
     * 监控指标的样本值
     */
    @Column(name = "alert_value")
    private String alertValue;

    /**
     * 事件检查项
     */
    @Column(name = "alert_check")
    private String alertCheck;

    /**
     * 告警事件的来源
     */
    @Column(name = "alert_source")
    private String alertSource;

    /**
     * 探测端类型
     */
    @Column(name = "alert_type")
    private String alertType;

    /**
     * 服务名称
     */
    private String service;

    /**
     * 事件开始时间的时间戳
     */
    @Column(name = "alert_time")
    private Date alertTime;

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
     * 告警元数据
     */
    private String metadata;
}