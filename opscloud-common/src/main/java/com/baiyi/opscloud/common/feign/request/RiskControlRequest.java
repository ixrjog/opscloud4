package com.baiyi.opscloud.common.feign.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/7/5 2:57 PM
 * @Since 1.0
 */
public class RiskControlRequest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SerLoader {

        @Schema(description = "ser 包列表")
        private List<ReloadedSer> reloadingSerList;

        @Schema(description = "操作人")
        private String operator;

        @Schema(description = "任务ID")
        private Integer taskNo;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ReloadedSer {

        @Schema(description = "ser 包名称")
        private String serName;

        @Schema(description = "ser 包 MD5")
        private String fileMd5;

        @Schema(description = "ser 包存储 S3 bucket")
        private String bucketName;

        @Schema(description = "ser 包存储 S3 keyName")
        private String keyName;

    }

}