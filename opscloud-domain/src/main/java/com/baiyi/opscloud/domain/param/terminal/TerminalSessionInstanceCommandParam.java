package com.baiyi.opscloud.domain.param.terminal;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/8/3 9:35 上午
 * @Version 1.0
 */
public class TerminalSessionInstanceCommandParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class InstanceCommandPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "会话实例ID")
        @Min(value = 1,message = "必需指定会话实例ID")
        @NotNull
        private Integer terminalSessionInstanceId;

        @ApiModelProperty(value = "查询参数")
        private String queryName;

        private Boolean extend;

    }

}
