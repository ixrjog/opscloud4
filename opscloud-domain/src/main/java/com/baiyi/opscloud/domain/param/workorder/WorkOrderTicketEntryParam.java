package com.baiyi.opscloud.domain.param.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/1/11 4:00 PM
 * @Version 1.0
 */
public class WorkOrderTicketEntryParam {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class EntryQuery {

        @Schema(description = "数据实例UUID")
        private String instanceUuid;

        @Schema(description = "regionId")
        private String regionId;

        @Schema(description = "kind")
        private String kind;

        @Schema(description = "资产类型")
        private String assetType;

        @Schema(description = "工单票据ID")
        private Integer workOrderTicketId;

        @Schema(description = "查询名称")
        private String queryName;

        @Schema(description = "查询条目数量")
        @Builder.Default
        private Integer length = 20;

        @Schema(description = "应用ID")
        private Integer applicationId;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class TicketEntry extends WorkOrderTicketEntry implements Serializable {

        @Serial
        private static final long serialVersionUID = 7854793943421192263L;

        private Map<String, String> properties;

    }

}