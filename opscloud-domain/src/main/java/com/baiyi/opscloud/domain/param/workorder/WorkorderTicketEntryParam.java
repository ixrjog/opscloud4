package com.baiyi.opscloud.domain.param.workorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/28 11:43 上午
 * @Version 1.0
 */
public class WorkorderTicketEntryParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TicketEntry<T> {

        @ApiModelProperty(value = "id")
        private Integer id;

        @ApiModelProperty(value = "工单组名称")
        private String entryKey;

        @ApiModelProperty(value = "票据id")
        private Integer ticketId;

        @ApiModelProperty(value = "条目对象")
        private T ticketEntry;

    }
}
