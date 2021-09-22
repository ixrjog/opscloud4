package com.baiyi.opscloud.domain.param.task;

import com.baiyi.opscloud.domain.vo.server.ServerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:26 下午
 * @Version 1.0
 */
public class ServerTaskParam {

    @Data
    @Builder
    @ApiModel
    public static class SubmitServerTask {

        @ApiModelProperty(value = "执行实例")
        private String instanceUuid;

        @ApiModelProperty(value = "剧本id", example = "1")
        private Integer ansiblePlaybookId;

        @ApiModelProperty(value = "任务类型")
        private String taskType;

        private String vars;

        private String tags;

        private List<ServerVO.Server> servers; // 执行任务的服务器

    }
}
