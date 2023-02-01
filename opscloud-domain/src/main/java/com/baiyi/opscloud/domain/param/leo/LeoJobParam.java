package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:42
 * @Version 1.0
 */
public class LeoJobParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class JobPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "名称")
        private String queryName;

        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @ApiModelProperty(value = "模板ID")
        private Integer templateId;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "标签ID")
        private Integer tagId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        private Boolean extend;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class JobBuildPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "名称")
        private String queryName;

        @Min(value = 0, message = "任务ID不能为空")
        @ApiModelProperty(value = "任务ID")
        private Integer jobId;

        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "构建结果")
        private String buildResult;

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        private Boolean extend;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class JobDeployPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "名称")
        private String queryName;

        @Min(value = 0, message = "应用ID不能为空")
        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "部署结果")
        private String deployResult;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        private List<Integer> jobIds;

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        private Boolean extend;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddJob {

        private Integer id;

        @Min(value = 0, message = "关联任务ID不能为空")
        @ApiModelProperty(value = "关联任务ID")
        private Integer parentId;

        @Min(value = 1, message = "必须指定应用ID")
        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @NotEmpty(message = "显示名称不能为空")
        @ApiModelProperty(value = "显示名称")
        private String name;

        @ApiModelProperty(value = "默认分支")
        private String branch;

        @Min(value = 0, message = "必须指定环境类型")
        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @NotNull
        @ApiModelProperty(value = "当前构建编号")
        private Integer buildNumber;

        @NotNull
        @ApiModelProperty(value = "模板版本")
        private String templateVersion;

        @NotNull
        @ApiModelProperty(value = "任务Key")
        private String jobKey;

        @NotNull
        @ApiModelProperty(value = "隐藏任务")
        private Boolean hide;

        @NotNull
        @ApiModelProperty(value = "模版ID")
        private Integer templateId;

        @NotNull
        @ApiModelProperty(value = "任务超文本链接")
        private String href;

        @NotNull
        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "任务参数配置")
        private String jobConfig;

        @ApiModelProperty(value = "模板内容")
        private String templateContent;

        @ApiModelProperty(value = "描述")
        private String comment;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateJob {

        private Integer id;

        @Min(value = 0, message = "关联任务ID不能为空")
        @ApiModelProperty(value = "关联任务ID")
        private Integer parentId;

        @Min(value = 1, message = "必须指定应用ID")
        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @NotEmpty(message = "显示名称不能为空")
        @ApiModelProperty(value = "显示名称")
        private String name;

        @ApiModelProperty(value = "默认分支")
        private String branch;

        @Min(value = 0, message = "必须指定环境类型")
        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "当前构建编号")
        private Integer buildNumber;

        @ApiModelProperty(value = "模板版本")
        private String templateVersion;

        @ApiModelProperty(value = "隐藏任务")
        private Boolean hide;

        @ApiModelProperty(value = "模版ID")
        private Integer templateId;

        @ApiModelProperty(value = "任务超文本链接")
        private String href;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "任务参数配置")
        private String jobConfig;

        @ApiModelProperty(value = "模板内容")
        private String templateContent;

        @ApiModelProperty(value = "描述")
        private String comment;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class CloneJob {

        @Min(value = 1, message = "必须指定源应用ID")
        @ApiModelProperty(value = "源应用ID")
        private Integer srcApplicationId;

        @Min(value = 1, message = "必须指定目标应用ID")
        @ApiModelProperty(value = "目标应用ID")
        private Integer  destApplicationId;

        @ApiModelProperty(value = "克隆标签")
        private Boolean cloneTag;

    }

}
