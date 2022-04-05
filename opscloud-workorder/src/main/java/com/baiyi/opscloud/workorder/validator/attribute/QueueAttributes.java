package com.baiyi.opscloud.workorder.validator.attribute;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/4/5 12:55
 * @Version 1.0
 */
@Data
@Builder
public class QueueAttributes {

    @NotNull
    @Size(max = 900, message = "交付延迟时间应介于 0 秒至 15 分钟之间")
    private String delaySeconds;

    @NotNull
    @Size(min = 1024, max = 262144, message = "最大消息大小应介于 1 KB 和 256 KB之间")
    private String maximumMessageSize;

    @NotNull
    @Size(min = 60, max = 1209600, message = "消息保留周期应介于 1 分钟至 14 天之间")
    private String messageRetentionPeriod;

    @NotNull
    @Size(max = 20, message = "接收消息等待时间应介于 0 至 20 秒之间")
    private String receiveMessageWaitTimeSeconds;

    @NotNull
    @Size(max = 43200, message = "可见性超时时间应介于 0 秒至 12 小时之间")
    private String visibilityTimeout;

    public static QueueAttributes toAttributes(Map<String, String> attributes) {
        return QueueAttributes.builder()
                .delaySeconds(attributes.get("DelaySeconds"))
                .maximumMessageSize(attributes.get("MaximumMessageSize"))
                .messageRetentionPeriod("MessageRetentionPeriod")
                .receiveMessageWaitTimeSeconds("ReceiveMessageWaitTimeSeconds")
                .visibilityTimeout("VisibilityTimeout")
                .build();
    }

}