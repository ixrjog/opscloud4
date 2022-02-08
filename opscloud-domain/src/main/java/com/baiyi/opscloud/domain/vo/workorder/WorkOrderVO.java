package com.baiyi.opscloud.domain.vo.workorder;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
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
    @ApiModel
    public static class WorkOrder extends BaseVO {

        private WorkOrderVO.Group workOrderGroup;

        private Integer id;

        @ApiModelProperty(value = "工单名称")
        @NotEmpty(message = "工单名称不能为空")
        private String name;

        @ApiModelProperty(value = "顺序")
        private Integer seq;

        @ApiModelProperty(value = "图标")
        private String icon;

        @ApiModelProperty(value = "工单Key")
        private String workOrderKey;

        @ApiModelProperty(value = "帮助文档id")
        private Integer sysDocumentId;

        @ApiModelProperty(value = "工单组ID")
        private Integer workOrderGroupId;

        @ApiModelProperty(value = "状态 0 正常 1 开发 2 停用 3")
        private Integer status;

        @ApiModelProperty(value = "说明")
        private String comment;

        private Boolean isActive;

        @ApiModelProperty(value = "工作流配置")
        private String workflow;

        /**
         * 前端选择用
         */
        private Boolean loading;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Group extends BaseVO {

        @ApiModelProperty(value = "工单数量", example = "1")
        private Integer workOrderSize;

        @ApiModelProperty(value = "组成员工单")
        private List<WorkOrder> workOrders;

        private Integer id;

        @ApiModelProperty(value = "工单组名称")
        @NotEmpty(message = "工单组名称不能为空")
        private String name;

        @ApiModelProperty(value = "顺序")
        private Integer seq;

        @ApiModelProperty(value = "图标")
        private String icon;

        @ApiModelProperty(value = "工单组类型")
        private Integer groupType;

        @ApiModelProperty(value = "说明")
        private String comment;
    }
}
