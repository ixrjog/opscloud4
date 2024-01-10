package com.baiyi.opscloud.domain.param.workorder;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/2/8 9:07 AM
 * @Version 1.0
 */
public class WorkOrderGroupParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Group {

        private Integer id;

        @Schema(description = "工单组名称")
        @NotEmpty(message = "工单组名称不能为空")
        private String name;

        @Schema(description = "工单组名称国际化")
        @NotEmpty(message = "工单组名称国际化不能为空")
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

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class WorkOrderGroupPageQuery extends PageParam implements IExtend {

        @Schema(description = "展开")
        private Boolean extend;

    }

}