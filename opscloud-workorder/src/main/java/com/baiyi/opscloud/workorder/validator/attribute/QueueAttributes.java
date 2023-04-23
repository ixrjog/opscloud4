package com.baiyi.opscloud.workorder.validator.attribute;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/4/5 12:55
 * @Version 1.0
 */
@Data
@Builder
public class QueueAttributes implements IAttributeValidator {

    // 校验工单条目失败: 消息保留周期应介于1分钟至14天之间、最大消息大小应介于1KB和256KB之间

    @Range(max = 900, message = "交付延迟时间应介于0秒至15分钟之间")
    private Integer delaySeconds;

    @Range(min = 1024, max = 262144, message = "最大消息大小应介于1KB和256KB之间")
    private Integer maximumMessageSize;

    @Range(min = 60, max = 1209600, message = "消息保留周期应介于1分钟至14天之间")
    private Integer messageRetentionPeriod;

    @Range(max = 20, message = "接收消息等待时间应介于0至20秒之间")
    private Integer receiveMessageWaitTimeSeconds;

    @Range(max = 43200, message = "可见性超时时间应介于0秒至12小时之间")
    private Integer visibilityTimeout;

    public static QueueAttributes toAttributes(Map<String, String> attributes) {
        return QueueAttributes.builder()
                .delaySeconds(Integer.parseInt(attributes.get("DelaySeconds")))
                .maximumMessageSize(Integer.parseInt(attributes.get("MaximumMessageSize")))
                .messageRetentionPeriod(Integer.parseInt(attributes.get("MessageRetentionPeriod")))
                .receiveMessageWaitTimeSeconds(Integer.parseInt(attributes.get("ReceiveMessageWaitTimeSeconds")))
                .visibilityTimeout(Integer.parseInt(attributes.get("VisibilityTimeout")))
                .build();
    }

}