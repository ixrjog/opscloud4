package com.baiyi.opscloud.domain.param.kubernetes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/10/8 15:51
 * @Version 1.0
 */
public class KubernetesIstioParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GetResource implements BaseKubernetesParam.IResource {

        @NotNull(message = "数据源实例ID不能为空")
        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        @Schema(description = "命名空间")
        private String namespace;

        @Schema(description = "资源名称")
        private String name;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DeleteResource implements BaseKubernetesParam.IResource {

        @NotNull(message = "数据源实例ID不能为空")
        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        @Schema(description = "命名空间")
        private String namespace;

        @Schema(description = "资源名称")
        private String name;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CreateResource implements BaseKubernetesParam.IStreamResource {

        @NotNull(message = "数据源实例ID不能为空")
        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        private String resourceYaml;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateResource implements BaseKubernetesParam.IStreamResource {

        @NotNull(message = "数据源实例ID不能为空")
        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        private String resourceYaml;

    }

}