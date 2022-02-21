package com.baiyi.opscloud.domain.param.workorder;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/1/26 2:06 PM
 * @Version 1.0
 */
public class WorkOrderParam {

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class WorkOrderPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "工单组ID")
        private Integer workOrderGroupId;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

    }

}
