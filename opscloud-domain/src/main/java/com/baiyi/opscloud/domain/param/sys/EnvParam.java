package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:30 下午
 * @Version 1.0
 */
public class EnvParam {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Env {

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "环境名称")
        private String envName;

        @Schema(description = "颜色值")
        private String color;

        @Schema(description = "终端提示色")
        private Integer promptColor;

        @Schema(description = "环境值", example = "1")
        private Integer envType;

        @Schema(description = "有效", example = "true")
        private Boolean isActive;

        @Schema(description = "顺序")
        private Integer seq;

        @Schema(description = "描述")
        private String comment;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class EnvPageQuery extends PageParam {

        @Schema(description = "环境名称")
        private String envName;

        @Schema(description = "环境值")
        private Integer envType;

        @Schema(description = "有效")
        private Boolean isActive;

    }

}