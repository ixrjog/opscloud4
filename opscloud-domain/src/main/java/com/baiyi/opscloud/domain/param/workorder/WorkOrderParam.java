package com.baiyi.opscloud.domain.param.workorder;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2022/1/26 2:06 PM
 * @Version 1.0
 */
public class WorkOrderParam {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class WorkOrder  {

        private Integer id;

        @Schema(description = "工单名称")
        @NotEmpty(message = "工单名称不能为空")
        private String name;

        @Schema(description = "工单名称国际化")
        @NotEmpty(message = "工单名称国际化不能为空")
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

    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class WorkOrderPageQuery extends PageParam implements IExtend {

        @Schema(description = "工单组ID")
        private Integer workOrderGroupId;

        @Schema(description = "展开")
        private Boolean extend;

    }

}