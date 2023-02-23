package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.param.IExtend;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
    public static class DoBuild {

        @Min(value = 0, message = "关联任务ID不能为空")
        @ApiModelProperty(value = "关联任务ID")
        private Integer jobId;

        @ApiModelProperty(value = "分支")
        private String branch;

        @ApiModelProperty(value = "COMMIT ID")
        private String commitId;

        @ApiModelProperty(value = "构建参数")
        private Map<String, String> params;

        @ApiModelProperty(value = "版本名称")
        private String versionName;

        @ApiModelProperty(value = "版本说明")
        private String versionDesc;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDeployVersion implements IExtend {

        @Min(value = 0, message = "任务ID不能为空")
        @ApiModelProperty(value = "任务ID")
        private Integer jobId;

        @ApiModelProperty(value = "名称")
        private String queryName;

        private Boolean extend;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDeployDeployment {

        @Min(value = 0, message = "任务ID不能为空")
        @ApiModelProperty(value = "任务ID")
        private Integer jobId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDeployDeploymentVersion  {

        @Min(value = 0, message = "任务ID不能为空")
        @ApiModelProperty(value = "任务ID")
        private Integer jobId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetBuildBranchOptions {

        @Min(value = 0, message = "关联任务ID不能为空")
        @ApiModelProperty(value = "关联任务ID")
        private Integer jobId;

        @NotEmpty(message = "必须指定项目SshURL")
        @ApiModelProperty(value = "项目SshURL")
        private String sshUrl;

        @ApiModelProperty(value = "查询Tag")
        private Boolean openTag;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBuildBranch {

        @Min(value = 0, message = "关联任务ID不能为空")
        @ApiModelProperty(value = "关联任务ID")
        private Integer jobId;

        @NotEmpty(message = "必须指定项目SshURL")
        @ApiModelProperty(value = "项目SshURL")
        private String sshUrl;

        @NotEmpty(message = "必须指定从哪个分支创建")
        @ApiModelProperty(value = "从这个分支创建")
        private String ref;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBuild {

        @Min(value = 0, message = "构建ID不能为空")
        @ApiModelProperty(value = "构建ID")
        private Integer id;

        @NotEmpty(message = "版本名称不能为空")
        @ApiModelProperty(value = "版本名称")
        private String versionName;

        @ApiModelProperty(value = "版本描述")
        private String versionDesc;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

    }

}
