package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Table(name = "alert_notify_history")
public class AlertNotifyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 告警事件id
     */
    @Column(name = "alert_notify_event_id")
    private Integer alertNotifyEventId;

    /**
     * 告警通知媒介
     */
    @Column(name = "alert_notify_media")
    private String alertNotifyMedia;

    private String username;

    /**
     * 告警通知状态
     */
    @Column(name = "alert_notify_status")
    private String alertNotifyStatus;

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