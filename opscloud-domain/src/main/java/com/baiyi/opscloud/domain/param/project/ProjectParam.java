package com.baiyi.opscloud.domain.param.project;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @Author 修远
 * @Date 2023/5/12 5:37 PM
 * @Since 1.0
 */
public class ProjectParam {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddProject {

        @Schema(description = "项目名称")
        @NotBlank(message = "必须指定项目名称")
        private String name;

        @Schema(description = "项目类型")
        @NotBlank(message = "必须指定项目类型")
        private String projectType;

        @Schema(description = "项目状态")
        @NotBlank(message = "必须指定项目状态")
        private String projectStatus;

        @Schema(description = "开始时间")
        @NotNull(message = "必须指定项目开始时间")
        private Date startTime;

        @Schema(description = "结束时间")
        private Date endTime;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "描述")
        private String comment;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UpdateProject {

        private Integer id;

        @Schema(description = "项目名称")
        private String name;

        @Schema(description = "项目类型")
        private String projectType;

        @Schema(description = "项目状态")
        private String projectStatus;

        @Schema(description = "开始时间")
        private Date startTime;

        @Schema(description = "结束时间")
        private Date endTime;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "描述")
        private String comment;

    }

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ProjectPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "项目名称")
        private String queryName;

        @Schema(description = "标签ID")
        private Integer tagId;

        @Schema(description = "项目类型")
        private String projectType;

        @Schema(description = "项目状态")
        private String projectStatus;

        @Schema(description = "展开")
        private Boolean extend;

        private final int businessType = BusinessTypeEnum.PROJECT.getType();

    }

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ProjectApplication extends SuperPageParam implements IExtend, IRelation {

        @Schema(description = "项目名称")
        private String queryName;

        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "展开")
        private Boolean extend;

        @Schema(description = "展示资产关系")
        private Boolean relation;

    }

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ResProjectPageQuery extends SuperPageParam implements BaseBusiness.IBusiness, IExtend {

        @Schema(description = "项目名称")
        private String queryName;

        @Schema(description = "业务ID")
        private Integer businessId;

        @Schema(description = "业务类型")
        private Integer businessType;

        @Schema(description = "资源类型", example = "ProjectResTypeEnum.APPLICATION.name()")
        private String resourceType;

        @Schema(description = "展开")
        private Boolean extend;

    }

}