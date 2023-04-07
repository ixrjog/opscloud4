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

        @Schema(name = "数据实例UUID")
        private String instanceUuid;

        @Schema(name = "regionId")
        private String regionId;

        @Schema(name = "kind")
        private String kind;

        @Schema(name = "assetType")
        private String assetType;

        @Schema(name = "工单票据ID")
        private Integer workOrderTicketId;

        @Schema(name = "查询名称")
        private String queryName;

        @Schema(name = "查询条目数量")
        @Builder.Default
        private Integer length = 20;

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
