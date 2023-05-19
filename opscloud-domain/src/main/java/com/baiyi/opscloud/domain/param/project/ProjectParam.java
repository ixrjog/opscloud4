package com.baiyi.opscloud.domain.param.project;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

        private Integer id;

        @Schema(description = "项目名称")
        private String name;

        @Schema(description = "项目Key")
        private String projectKey;

        @Schema(description = "项目类型")
        private Integer projectType;

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
        private Integer projectType;

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
    public static class ProjectPageQuery extends SuperPageParam implements IExtend, IRelation {

        @Schema(description = "项目名称")
        private String queryName;

        @Schema(description = "标签ID")
        private Integer tagId;

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
