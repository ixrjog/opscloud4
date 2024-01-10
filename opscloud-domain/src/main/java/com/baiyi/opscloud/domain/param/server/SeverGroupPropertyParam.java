package com.baiyi.opscloud.domain.param.server;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/6/22 11:00 上午
 * @Version 1.0
 */
public class SeverGroupPropertyParam {
    
    @Data
    @NoArgsConstructor
    @Schema
    public static class PropertyParam {

        @Schema(description = "服务器组ID")
        @NotNull
        private Integer serverGroupId;

        @Schema(description = "属性名称")
        @NotNull
        private Set<String> propertyNameSet;

        @Schema(description = "环境类型",example = "-1")
        @NotNull
        private Integer envType;

    }
    
}