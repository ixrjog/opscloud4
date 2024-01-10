package com.baiyi.opscloud.domain.param.application;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.IAppResType;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2021/9/8 5:17 下午
 * @Version 1.0
 */
public class ApplicationResourceParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Resource {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @NotNull(message = "应用ID不能为空")
        private Integer applicationId;

        private String name;

        @Schema(description = "虚拟资源", example = "true")
        private Boolean virtualResource ;

        @NotNull(message = "资源类型不能为空")
        private String resourceType;

        @NotNull(message = "业务id不能为空")
        private Integer businessId;

        @NotNull(message = "业务类型不能为空")
        private Integer businessType;

        private String comment;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ResourcePageQuery extends SuperPageParam implements IExtend, BaseBusiness.IBusinessType, IAppResType {

        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        @Schema(description = "数据源实例UUID")
        private String instanceUuid;

        @Schema(description = "应用资源类型")
        @NotNull(message = "必须指定应用资源类型")
        private String appResType;

        @Schema(description = "业务类型")
        @NotNull(message = "必须指定业务类型")
        private Integer businessType;

        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "应用名称")
        private String queryName;

        @Schema(description = "展开")
        private Boolean extend;

    }

}