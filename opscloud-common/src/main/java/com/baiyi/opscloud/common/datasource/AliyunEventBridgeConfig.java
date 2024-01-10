package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/8/30 19:19
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AliyunEventBridgeConfig extends BaseDsConfig {

    private EventBridge eventBridge;

    @Data
    @NoArgsConstructor
    @Schema
    public static class EventBridge {

        private Leo leo;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Leo {

        @Schema(description = "https://XXXXXXX.eventbridge.eu-central-1.aliyuncs.com/webhook/putEvents")
        private String url;
        private String token;
        private String source;
        private String type;

        @Schema(description = "CloudEvents协议版本")
        private String specversion;

    }

}