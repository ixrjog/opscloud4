package com.baiyi.opscloud.domain.vo.workorder;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 1:21 PM
 * @Version 1.0
 */
public class WorkOrderVO {

    public interface IWorkOrder {

        Integer getWorkOrderId();

        void setWorkOrder(WorkOrderVO.WorkOrder workOrder);

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class WorkOrder extends BaseVO {

        private WorkOrderVO.Group workOrderGroup;

        private Integer id;

        @Schema(description = "工单名称")
        private String name;

        @Schema(description = "工单名称国际化")
        private String i18nEn;

        @Schema(description = "顺序")
        private Integer seq;

        @Schema(description = "图标")
        private String icon;

        @Schema(description = "工单Key")
        private String workOrderKey;

        @Schema(description = "帮助文档ID")
        private Integer sysDocumentId;

        @Schema(description = "工单组ID")
        private Integer workOrderGroupId;

        @Schema(description = "状态 0 正常 1 开发 2 停用 3")
        private Integer status;

        @Schema(description = "报表颜色")
        private String color;

        @Schema(description = "说明")
        private String comment;

        @Schema(description = "文档地址")
        private String docs;

        private Boolean isActive;

        @Schema(description = "工作流配置")
        private String workflow;

        /**
         * 前端选择用
         */
        private Boolean loading;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Group extends BaseVO {

        @Schema(description = "工单数量", example = "1")
        private Integer workOrderSize;

        @Schema(description = "组成员工单")
        private List<WorkOrder> workOrders;

        private Integer id;

        @Schema(description = "工单组名称")
        private String name;

        @Schema(description = "工单组名称国际化")
        private String i18nEn;

        @Schema(description = "顺序")
        private Integer seq;

        @Schema(description = "图标")
        private String icon;

        @Schema(description = "工单组类型")
        private Integer groupType;

        @Schema(description = "说明")
        private String comment;
    }

}