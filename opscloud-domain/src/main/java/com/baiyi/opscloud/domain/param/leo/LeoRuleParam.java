package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2023/1/10 17:47
 * @Version 1.0
 */
public class LeoRuleParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RulePageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "有效")
        private Boolean isActive;

        private Boolean extend;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateRule {

        @Min(value = 1, message = "必须指定规则ID")
        private Integer id;

        private String name;

        private Boolean isActive;

        private String ruleConfig;

        private String comment;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddRule {

        private Integer id;

        @NotEmpty(message = "规则名称不能为空")
        private String name;

        @Schema(description = "有效")
        @NotNull(message = "必须指定有效")
        private Boolean isActive;

        @NotEmpty(message = "规则配置不能为空")
        private String ruleConfig;

        private String comment;

    }

}