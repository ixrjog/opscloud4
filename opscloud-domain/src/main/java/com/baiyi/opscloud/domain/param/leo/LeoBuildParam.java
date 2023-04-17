package com.baiyi.opscloud.domain.param.leo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:16
 * @Version 1.0
 */
public class LeoBuildParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DoBuild {

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(description = "关联任务ID")
        private Integer jobId;

        @Schema(description = "分支")
        private String branch;

        @Schema(description = "COMMIT ID")
        private String commitId;

        @Schema(description = "构建参数")
        private Map<String, String> params;

        @Schema(description = "版本名称")
        private String versionName;

        @Schema(description = "版本说明")
        private String versionDesc;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GetBuildBranchOptions {

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(description = "关联任务ID")
        private Integer jobId;

        @NotEmpty(message = "必须指定项目SshURL")
        @Schema(description = "项目SshURL")
        private String sshUrl;

        @Schema(description = "查询Tag")
        private Boolean openTag;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CreateBuildBranch {

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(description = "关联任务ID")
        private Integer jobId;

        @NotEmpty(message = "必须指定项目SshURL")
        @Schema(description = "项目SshURL")
        private String sshUrl;

        @NotEmpty(message = "必须指定从哪个分支创建")
        @Schema(description = "从这个分支创建")
        private String ref;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateBuild {

        @Min(value = 0, message = "构建ID不能为空")
        @Schema(description = "构建ID")
        private Integer id;

        @NotEmpty(message = "版本名称不能为空")
        @Schema(description = "版本名称")
        private String versionName;

        @Schema(description = "版本描述")
        private String versionDesc;

        @Schema(description = "有效")
        private Boolean isActive;

    }

}
