package com.baiyi.opscloud.domain.param.task;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:26 下午
 * @Version 1.0
 */
public class ServerTaskParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ServerTaskPageQuery extends PageParam implements IExtend {

        @Schema(description = "关键字查询")
        private String queryName;

        @Schema(description = "任务是否完成")
        private Boolean finalized;

        private Boolean extend;

    }

    @Data
    @Builder
    @Schema
    public static class SubmitServerTask {

        @Schema(description = "执行实例")
        @NotNull(message = "必须指定Ansible实例")
        private String instanceUuid;

        @Schema(description = "任务UUID(幂等)")
        @NotNull(message = "必须指定任务uuid")
        private String taskUuid;

        @Schema(description = "剧本ID", example = "1")
        @NotNull(message = "必须指定任务剧本")
        private Integer ansiblePlaybookId;

        @Schema(description = "任务类型")
        private String taskType;

        private String vars;

        private String tags;

        @Schema(description = "执行任务的服务器列表")
        @NotNull(message = "必须指定执行任务的服务器列表")
        private List<ServerVO.Server> servers;

    }

}