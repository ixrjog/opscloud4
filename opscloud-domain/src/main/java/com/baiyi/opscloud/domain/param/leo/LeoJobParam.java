package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    @Schema
    public static class JobPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "名称")
        private String queryName;

        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "模板ID")
        private Integer templateId;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "构建类型")
        private String buildType;

        @Schema(description = "标签ID")
        private Integer tagId;

        @Schema(description = "环境类型")
        private Integer envType;

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        private Boolean extend;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class JobBuildPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "名称")
        private String queryName;

        @Min(value = 0, message = "任务ID不能为空")
        @Schema(description = "任务ID")
        private Integer jobId;

        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "构建结果")
        private String buildResult;

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        private Boolean extend;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MyJobBuildPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "名称")
        private String queryName;

        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "环境类型")
        private Integer envType;

        private Boolean extend;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class JobDeployPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "名称")
        private String queryName;

        @Min(value = 0, message = "应用ID不能为空")
        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "部署结果")
        private String deployResult;

        @Schema(description = "有效")
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
        @Schema(description = "关联任务ID")
        private Integer parentId;

        @Min(value = 1, message = "必须指定应用ID")
        @Schema(description = "应用ID")
        private Integer applicationId;

        @NotEmpty(message = "显示名称不能为空")
        @Schema(description = "显示名称")
        private String name;

        @Schema(description = "默认分支")
        private String branch;

        @Min(value = 0, message = "必须指定环境类型")
        @Schema(description = "环境类型")
        private Integer envType;

        @NotNull
        @Schema(description = "当前构建编号")
        private Integer buildNumber;

        @NotNull
        @Schema(description = "模板版本")
        private String templateVersion;

        @NotNull
        @Schema(description = "任务Key")
        private String jobKey;

        @NotNull
        @Schema(description = "隐藏任务")
        private Boolean hide;

        @NotNull
        @Schema(description = "模板ID")
        private Integer templateId;

        @NotNull
        @Schema(description = "任务超文本链接")
        private String href;

        @NotNull
        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "任务参数配置")
        private String jobConfig;

        @Schema(description = "模板内容")
        private String templateContent;

        @Schema(description = "描述")
        private String comment;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateJob {

        private Integer id;

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(description = "关联任务ID")
        private Integer parentId;

        @Min(value = 1, message = "必须指定应用ID")
        @Schema(description = "应用ID")
        private Integer applicationId;

        @NotEmpty(message = "显示名称不能为空")
        @Schema(description = "显示名称")
        private String name;

        @Schema(description = "默认分支")
        private String branch;

        @Min(value = 0, message = "必须指定环境类型")
        @Schema(description = "环境类型")
        private Integer envType;

        @Schema(description = "当前构建编号")
        private Integer buildNumber;

        @Schema(description = "模板版本")
        private String templateVersion;

        @Schema(description = "隐藏任务")
        private Boolean hide;

        @Schema(description = "模板ID")
        private Integer templateId;

        @Schema(description = "任务超文本链接")
        private String href;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "任务参数配置")
        private String jobConfig;

        @Schema(description = "模板内容")
        private String templateContent;

        @Schema(description = "描述")
        private String comment;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CloneJob {

        @Min(value = 1, message = "必须指定源应用ID")
        @Schema(description = "源应用ID")
        private Integer srcApplicationId;

        @Min(value = 1, message = "必须指定目标应用ID")
        @Schema(description = "目标应用ID")
        private Integer destApplicationId;

        @Schema(description = "克隆标签")
        private Boolean cloneTag;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CloneOneJob {

        @Min(value = 1, message = "必须指定任务ID")
        @Schema(description = "任务ID")
        private Integer jobId;

        @NotBlank(message = "必须指定任务名称")
        @Schema(description = "任务名称")
        private String jobName;

        @NotBlank(message = "必须指定任务配置")
        @Schema(description = "任务配置")
        private String jobConfig;

        @Schema(description = "克隆标签")
        private Boolean cloneTag;

    }

}