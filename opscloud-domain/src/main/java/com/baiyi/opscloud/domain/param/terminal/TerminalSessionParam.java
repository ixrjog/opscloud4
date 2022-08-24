package com.baiyi.opscloud.domain.param.terminal;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:23 下午
 * @Version 1.0
 */
public class TerminalSessionParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class TerminalSessionPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "会话类型")
        private String sessionType;

        @ApiModelProperty(value = "会话状态")
        private Boolean sessionClosed;

        @ApiModelProperty(value = "Opscloud实例名称")
        private String serverHostname;

        private Boolean extend;

    }

}
