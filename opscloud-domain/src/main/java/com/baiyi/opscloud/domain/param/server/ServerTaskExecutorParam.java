package com.baiyi.opscloud.domain.param.server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:30 下午
 * @Version 1.0
 */
public class ServerTaskExecutorParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class ServerTaskCommandExecutor extends TaskExecutor {

        @Schema(description = "命令")
        private String command;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class ServerTaskScriptExecutor extends TaskExecutor {

        @Schema(description = "指定执行的scriptId",example = "1")
        private Integer scriptId;

        @Schema(description = "自定义脚本参数")
        private String scriptParam;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class ServerTaskPlaybookExecutor extends TaskExecutor {

        @Schema(description = "指定执行的playbookId",example = "1")
        private Integer playbookId;

        @Schema(description = "自定义变量")
        private String vars;

        @Schema(description = "按标签执行playbook")
        private Set<String> tags;

    }

    @Data
    @Schema
    public static class TaskExecutor {

        // TASK_CONCURRENT
        @Schema(description = "并发线程数",example = "5")
        private Integer concurrent;

        @Schema(description = "系统用户")
        private String becomeUser;

        @Schema(description = "主机模式")
        private Set<String> hostPatterns;

        @Schema(description = "用户的服务器树缓存UUID")
        private String uuid;

        @Schema(description = "任务类型")
        private Integer taskType;

    }

}