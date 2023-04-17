package com.baiyi.opscloud.domain.param.application;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.IAppResType;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2021/9/8 5:17 下午
 * @Version 1.0
 */
public class ApplicationResourceParam {

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
