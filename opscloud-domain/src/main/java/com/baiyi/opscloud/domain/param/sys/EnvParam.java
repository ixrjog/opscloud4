package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
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
