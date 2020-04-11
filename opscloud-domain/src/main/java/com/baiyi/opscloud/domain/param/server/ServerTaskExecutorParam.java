package com.baiyi.opscloud.domain.param.server;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:30 下午
 * @Version 1.0
 */
public class ServerTaskExecutorParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerTaskCommandExecutor extends TaskExecutor {

        @ApiModelProperty(value = "命令")
        private String command;

    }


    @Data
    @ApiModel
    public static class TaskExecutor {

        // TASK_CONCURRENT
        @ApiModelProperty(value = "并发线程数",example = "5")
        private Integer concurrent;

        @ApiModelProperty(value = "系统用户")
        private String becomeUser;

        @ApiModelProperty(value = "主机模式")
        private Set<String> hostPatterns;

        @ApiModelProperty(value = "用户的服务器树缓存uuid")
        private String uuid;

        @ApiModelProperty(value = "任务类型")
        private Integer taskType;
    }


}
