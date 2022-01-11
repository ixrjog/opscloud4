package com.baiyi.opscloud.domain.param.workorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/1/11 4:00 PM
 * @Version 1.0
 */
public class WorkOrderTicketEntryParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class EntryQuery {

        @ApiModelProperty(value = "数据实例UUID")
        private String instanceUuid;

        @ApiModelProperty(value = "工单票据ID")
        private Integer workOrderTicketId;

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "查询条目数量")
        private Integer length;

    }

}
