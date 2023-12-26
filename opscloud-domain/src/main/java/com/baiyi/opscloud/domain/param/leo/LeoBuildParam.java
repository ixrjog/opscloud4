package com.baiyi.opscloud.domain.param.leo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    public interface IAutoBuild {

        /**
         * 自动部署
         *
         * @return
         */
        Boolean getAutoBuild();

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DoBuild implements IAutoBuild, LeoDeployParam.IAutoDeploy {

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

        @Schema(description = "自动构建")
        private Boolean autoBuild;

        @Schema(description = "自动部署")
        @NotNull(message = "必须指定是否自动部署参数")
        private Boolean autoDeploy;

        @Schema(description = "Deployment资产ID，若启用autoDeploy则必须指定参数")
        private Integer assetId;

        @Schema(description = "项目ID")
        private Integer projectId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DoAutoBuild {

        @Schema(description = "执行用户")
        private String username;

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

        @Schema(description = "自动构建")
        private Boolean autoBuild;

        @Schema(description = "自动部署")
        @NotNull(message = "必须指定是否自动部署参数")
        private Boolean autoDeploy;

        @Schema(description = "Deployment资产ID，若启用autoDeploy则必须指定参数")
        private Integer assetId;

        @Schema(description = "项目ID")
        private Integer projectId;

        public LeoBuildParam.DoBuild toDoBuild() {
            return DoBuild.builder()
                    .jobId(jobId)
                    .branch(branch)
                    .commitId(commitId)
                    .params(params)
                    .versionName(versionName)
                    .versionDesc(versionDesc)
                    .autoBuild(autoBuild)
                    .autoDeploy(autoDeploy)
                    .assetId(assetId)
                    .projectId(projectId)
                    .build();
        }

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
    @Schema(name = "比较分支参数")
    public static class CompareBranch {

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(description = "关联任务ID")
        private Integer jobId;

        @NotEmpty(message = "必须指定项目SshURL")
        @Schema(description = "项目SshURL")
        private String sshUrl;

        @Schema(description = "当前构建分支")
        private String from;

        @Schema(description = "目标分支,省略参数使用master")
        private String to;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GetBuildMavenPublishInfo {

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(description = "关联任务ID")
        private Integer jobId;

        @NotEmpty(message = "必须指定项目SshURL")
        @Schema(description = "项目SshURL")
        private String sshUrl;

        @NotEmpty(message = "必须指定Ref")
        @Schema(description = "Ref: branch or tag")
        private String ref;

        @Schema(description = "构建工具")
        private BuildTools tools;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuildTools {

        private BuildToolsVersion version;

        @Schema(description = "工具类型: gradle、maven")
        private String type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuildToolsVersion {
        private String file;
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