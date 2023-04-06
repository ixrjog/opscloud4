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

        @Schema(name = "命令")
        private String command;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class ServerTaskScriptExecutor extends TaskExecutor {

        @Schema(name = "指定执行的scriptId",example = "1")
        private Integer scriptId;

        @Schema(name = "自定义脚本参数")
        private String scriptParam;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class ServerTaskPlaybookExecutor extends TaskExecutor {

        @Schema(name = "指定执行的playbookId",example = "1")
        private Integer playbookId;

        @Schema(name = "自定义变量")
        private String vars;

        @Schema(name = "按标签执行playbook")
        private Set<String> tags;

    }

    @Data
    @Schema
    public static class TaskExecutor {

        // TASK_CONCURRENT
        @Schema(name = "并发线程数",example = "5")
        private Integer concurrent;

        @Schema(name = "系统用户")
        private String becomeUser;

        @Schema(name = "主机模式")
        private Set<String> hostPatterns;

        @Schema(name = "用户的服务器树缓存uuid")
        private String uuid;

        @Schema(name = "任务类型")
        private Integer taskType;

    }


}
