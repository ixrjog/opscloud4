package com.baiyi.opscloud.domain.param.leo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public static class GetBuildBranchOptions {

        @Min(value = 0, message = "关联任务ID不能为空")
        @ApiModelProperty(value = "关联任务ID")
        private Integer jobId;

        @NotEmpty(message = "必须指定项目SshUrl")
        @ApiModelProperty(value = "项目SshUrl")
        private String sshUrl;

        @ApiModelProperty(value = "查询Tag")
        private Boolean openTag;

    }

}
