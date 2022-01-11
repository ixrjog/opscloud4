package com.baiyi.opscloud.domain.param.workorder;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/1/10 2:42 PM
 * @Version 1.0
 */
public class WorkOrderTicketParam {

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
