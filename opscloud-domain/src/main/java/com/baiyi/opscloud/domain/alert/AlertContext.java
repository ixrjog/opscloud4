package com.baiyi.opscloud.domain.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/7/21 9:37 AM
 * @Since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertContext {

    /**
     * 告警事件UUID
     */
    private String eventUuid;

    /**
     * 告警的名称
     */
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
     * 监控指标的样本值。
     */
    private String value;

    /**
     * 事件检查项
     */
    private String check;

    /**
     * 告警事件的来源
     */
    private String source;

    /**
     * 探测端类型
     */
    private String alertType;

    /**
     * 服务
     */
    private String service;

    /**
     * 事件开始时间的时间戳
     */
    private Long alertTime;

    private List<Metadata> metadata;

}