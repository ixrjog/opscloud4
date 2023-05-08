package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.param.IExtend;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2022/12/5 17:31
 * @Version 1.0
 */
public class LeoDeployParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DoDeploy {

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(description = "关联任务ID")
        private Integer jobId;

        @Schema(description = "构建ID")
        private Integer buildId;

        @Min(value = 0, message = "Deployment资产ID不能为空")
        @Schema(description = "Deployment资产ID")
        private Integer assetId;

        @Schema(description = "部署类型")
        private String deployType;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "自动部署")
    public static class DoAutoDeploy {

        @Schema(description = "执行用户")
        private String username;

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(description = "关联任务ID")
        private Integer jobId;

        @Schema(description = "构建ID")
        private Integer buildId;

        @Min(value = 0, message = "Deployment资产ID不能为空")
        @Schema(description = "Deployment资产ID")
        private Integer assetId;

        @Schema(description = "部署类型")
        private String deployType;

        public DoDeploy toDoDeploy(){
            return DoDeploy.builder()
                    .assetId(assetId)
                    .buildId(buildId)
                    .jobId(jobId)
                    .deployType(deployType)
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryDeployDeployment {

        @Min(value = 0, message = "任务ID不能为空")
        @Schema(description = "任务ID")
        private Integer jobId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDeployVersion implements IExtend {

        @Min(value = 0, message = "任务ID不能为空")
        @Schema(description = "任务ID")
        private Integer jobId;

        @Schema(description = "名称")
        private String queryName;

        private Boolean extend;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryDeployDeploymentVersion {

        @Min(value = 0, message = "任务ID不能为空")
        @Schema(description = "任务ID")
        private Integer jobId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CloneDeployDeployment {

        @NotNull(message = "资产ID不能为空")
        @Min(value = 0, message = "资产ID不能为空")
        @Schema(description = "资产ID")
        private Integer assetId;

        @NotNull(message = "构建ID不能为空")
        @Schema(description = "构建ID")
        private Integer buildId;

        private Integer jobId;

        private String name;

        @Schema(description = "无状态名称")
        private String deploymentName;

        @NotNull(message = "副本数量不能为空")
        @Schema(description = "副本数量")
        private Integer replicas;

    }

}
