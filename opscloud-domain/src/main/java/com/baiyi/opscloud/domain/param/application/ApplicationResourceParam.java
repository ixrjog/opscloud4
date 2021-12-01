package com.baiyi.opscloud.domain.param.application;

import com.baiyi.opscloud.domain.base.IApplicationResourceType;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/9/8 5:17 下午
 * @Version 1.0
 */
public class ApplicationResourceParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class ResourcePageQuery extends PageParam implements IExtend, BaseBusiness.IBusinessType, IApplicationResourceType {

        @ApiModelProperty(value = "数据源实例id")
        private Integer instanceId;

        @ApiModelProperty(value = "数据源实例uuid")
        private String instanceUuid;

        @ApiModelProperty(value = "应用资源类型")
        @NotNull(message = "必须指定应用资源类型")
        private String applicationResType;

        @ApiModelProperty(value = "业务类型")
        @NotNull(message = "必须指定业务类型")
        private Integer businessType;

        @ApiModelProperty(value = "应用id")
        private Integer applicationId;

        @ApiModelProperty(value = "应用名称")
        private String queryName;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

    }
}
