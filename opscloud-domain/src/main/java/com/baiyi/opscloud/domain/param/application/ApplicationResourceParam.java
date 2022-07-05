package com.baiyi.opscloud.domain.param.application;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.base.IApplicationResourceType;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

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
    @ApiModel
    public static class ResourcePageQuery extends SuperPageParam implements IExtend, BaseBusiness.IBusinessType, IApplicationResourceType {

        @ApiModelProperty(value = "数据源实例ID")
        private Integer instanceId;

        @ApiModelProperty(value = "数据源实例UUID")
        private String instanceUuid;

        @ApiModelProperty(value = "应用资源类型")
        @NotNull(message = "必须指定应用资源类型")
        private String applicationResType;

        @ApiModelProperty(value = "业务类型")
        @NotNull(message = "必须指定业务类型")
        private Integer businessType;

        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @ApiModelProperty(value = "应用名称")
        private String queryName;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

    }

}
