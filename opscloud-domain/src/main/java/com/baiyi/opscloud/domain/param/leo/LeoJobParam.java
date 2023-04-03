package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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

        @Schema(name = "名称")
        private String queryName;

        @Schema(name = "应用ID")
        private Integer applicationId;

        @Schema(name = "模板ID")
        private Integer templateId;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "标签ID")
        private Integer tagId;

        @Schema(name = "环境类型")
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

        @Schema(name = "名称")
        private String queryName;

        @Min(value = 0, message = "任务ID不能为空")
        @Schema(name = "任务ID")
        private Integer jobId;

        @Schema(name = "应用ID")
        private Integer applicationId;

        @Schema(name = "环境类型")
        private Integer envType;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "构建结果")
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
    public static class JobDeployPageQuery extends SuperPageParam implements IExtend {

        @Schema(name = "名称")
        private String queryName;

        @Min(value = 0, message = "应用ID不能为空")
        @Schema(name = "应用ID")
        private Integer applicationId;

        @Schema(name = "环境类型")
        private Integer envType;

        @Schema(name = "部署结果")
        private String deployResult;

        @Schema(name = "有效")
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
        @Schema(name = "关联任务ID")
        private Integer parentId;

        @Min(value = 1, message = "必须指定应用ID")
        @Schema(name = "应用ID")
        private Integer applicationId;

        @NotEmpty(message = "显示名称不能为空")
        @Schema(name = "显示名称")
        private String name;

        @Schema(name = "默认分支")
        private String branch;

        @Min(value = 0, message = "必须指定环境类型")
        @Schema(name = "环境类型")
        private Integer envType;

        @NotNull
        @Schema(name = "当前构建编号")
        private Integer buildNumber;

        @NotNull
        @Schema(name = "模板版本")
        private String templateVersion;

        @NotNull
        @Schema(name = "任务Key")
        private String jobKey;

        @NotNull
        @Schema(name = "隐藏任务")
        private Boolean hide;

        @NotNull
        @Schema(name = "模板ID")
        private Integer templateId;

        @NotNull
        @Schema(name = "任务超文本链接")
        private String href;

        @NotNull
        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "任务参数配置")
        private String jobConfig;

        @Schema(name = "模板内容")
        private String templateContent;

        @Schema(name = "描述")
        private String comment;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateJob {

        private Integer id;

        @Min(value = 0, message = "关联任务ID不能为空")
        @Schema(name = "关联任务ID")
        private Integer parentId;

        @Min(value = 1, message = "必须指定应用ID")
        @Schema(name = "应用ID")
        private Integer applicationId;

        @NotEmpty(message = "显示名称不能为空")
        @Schema(name = "显示名称")
        private String name;

        @Schema(name = "默认分支")
        private String branch;

        @Min(value = 0, message = "必须指定环境类型")
        @Schema(name = "环境类型")
        private Integer envType;

        @Schema(name = "当前构建编号")
        private Integer buildNumber;

        @Schema(name = "模板版本")
        private String templateVersion;

        @Schema(name = "隐藏任务")
        private Boolean hide;

        @Schema(name = "模板ID")
        private Integer templateId;

        @Schema(name = "任务超文本链接")
        private String href;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "任务参数配置")
        private String jobConfig;

        @Schema(name = "模板内容")
        private String templateContent;

        @Schema(name = "描述")
        private String comment;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CloneJob {

        @Min(value = 1, message = "必须指定源应用ID")
        @Schema(name = "源应用ID")
        private Integer srcApplicationId;

        @Min(value = 1, message = "必须指定目标应用ID")
        @Schema(name = "目标应用ID")
        private Integer  destApplicationId;

        @Schema(name = "克隆标签")
        private Boolean cloneTag;

    }

}
