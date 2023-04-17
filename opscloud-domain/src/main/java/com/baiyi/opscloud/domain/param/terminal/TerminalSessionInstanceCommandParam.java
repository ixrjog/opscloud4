package com.baiyi.opscloud.domain.param.terminal;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/8/3 9:35 上午
 * @Version 1.0
 */
public class TerminalSessionInstanceCommandParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class InstanceCommandPageQuery extends PageParam implements IExtend {

        @Schema(description = "会话实例ID")
        @Min(value = 1,message = "必需指定会话实例ID")
        @NotNull
        private Integer terminalSessionInstanceId;

        @Schema(description = "查询参数")
        private String queryName;

        private Boolean extend;

    }

}