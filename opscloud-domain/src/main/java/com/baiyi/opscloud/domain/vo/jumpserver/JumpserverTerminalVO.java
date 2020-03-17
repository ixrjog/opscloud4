package com.baiyi.opscloud.domain.vo.jumpserver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/13 5:49 下午
 * @Version 1.0
 */
public class JumpserverTerminalVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Terminal{

        @ApiModelProperty(value = "主键",example="格式为uuid")
        private String id;

        private String name;

        private String remoteAddr;

        private Integer sshPort;

        private Integer httpPort;

        private String comment;

        @ApiModelProperty(value = "当前活动会话",example="1")
        private Integer sessions;
    }
}
