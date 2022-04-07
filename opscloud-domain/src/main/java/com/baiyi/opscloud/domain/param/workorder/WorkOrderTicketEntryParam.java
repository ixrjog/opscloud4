package com.baiyi.opscloud.domain.param.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
    @ApiModel
    public static class EntryQuery {

        @ApiModelProperty(value = "数据实例UUID")
        private String instanceUuid;

        @ApiModelProperty(value = "regionId")
        private String regionId;

        @ApiModelProperty(value = "kind")
        private String kind;

        @ApiModelProperty(value = "assetType")
        private String assetType;

        @ApiModelProperty(value = "工单票据ID")
        private Integer workOrderTicketId;

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "查询条目数量")
        @Builder.Default
        private Integer length = 20;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class TicketEntry extends WorkOrderTicketEntry implements Serializable {

        private static final long serialVersionUID = 7854793943421192263L;

        private Map<String, String> properties;

    }

}
