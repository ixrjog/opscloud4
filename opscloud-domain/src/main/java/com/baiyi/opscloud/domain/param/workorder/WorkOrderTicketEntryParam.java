package com.baiyi.opscloud.domain.param.workorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class TicketEntry {
        private Integer id;

        private Integer workOrderTicketId;

        private String name;

        private String instanceUuid;

        private Integer businessType;

        private Integer businessId;

        private Integer entryStatus;

        private String entryKey;

        /**
         * 角色
         */
        private String role;

        /**
         * 说明
         */
        private String comment;

        /**
         * 内容
         */
        private String content;

        /**
         * 处理结果
         */
        private String result;
    }

}
