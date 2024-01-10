package com.baiyi.opscloud.datasource.aliyun.eventbridge.entity;

import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/8/31 13:17
 * @Version 1.0
 */
public class CloudEvents {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Event implements Serializable {

        @Serial
        private static final long serialVersionUID = 1647971767416400328L;

        private String id;

        private String type;

        private String source;

        @Schema(description = "CloudEvents协议版本")
        private String specversion;

        private LeoHook.DeployHook data;

    }

}