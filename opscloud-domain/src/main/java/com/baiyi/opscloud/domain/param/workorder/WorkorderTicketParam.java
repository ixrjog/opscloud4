package com.baiyi.opscloud.domain.param.workorder;

import com.baiyi.opscloud.domain.param.PageParam;
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
    public static class QueryMyTicket extends PageParam {

        @ApiModelProperty(value = "用户id")
        private Integer userId;
    }


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
