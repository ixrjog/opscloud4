package com.baiyi.opscloud.domain.param.workorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/28 7:03 下午
 * @Version 1.0
 */
public class WorkorderTicketParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CreateTicket {
        @ApiModelProperty(value = "工单key")
        private String workorderKey;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryTicket {
        @ApiModelProperty(value = "workorderTicketId")
        private Integer id;
    }



}
