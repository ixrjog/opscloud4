package com.baiyi.opscloud.domain.param.server;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/5/26 4:18 下午
 * @Version 1.0
 */
public class ServerChangeParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ExecuteServerChangeParam {

        @ApiModelProperty(value = "服务器组id")
        @NotNull
        private Integer serverGroupId;

        @ApiModelProperty(value = "上线/下线数量")
        @NotNull
        private Integer number;

        @ApiModelProperty(value = "任务id")
        @NotNull
        private String taskId;

    }


}
