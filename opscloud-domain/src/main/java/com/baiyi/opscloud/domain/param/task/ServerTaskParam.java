package com.baiyi.opscloud.domain.param.task;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
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
    @ApiModel
    public static class ServerTaskPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "任务是否完成")
        private Boolean finalized;

        private Boolean extend;

    }


    @Data
    @Builder
    @ApiModel
    public static class SubmitServerTask {

        @ApiModelProperty(value = "执行实例")
        @NotNull(message = "必须指定Ansible实例")
        private String instanceUuid;

        @ApiModelProperty(value = "任务uuid(幂等)")
        @NotNull(message = "必须指定任务uuid")
        private String taskUuid;

        @ApiModelProperty(value = "剧本id", example = "1")
        @NotNull(message = "必须指定任务剧本")
        private Integer ansiblePlaybookId;

        @ApiModelProperty(value = "任务类型")
        private String taskType;

        private String vars;

        private String tags;

        @ApiModelProperty(value = "执行任务的服务器列表")
        @NotNull(message = "必须指定执行任务的服务器列表")
        private List<ServerVO.Server> servers;

    }
}
