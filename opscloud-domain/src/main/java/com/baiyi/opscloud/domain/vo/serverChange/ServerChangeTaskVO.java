package com.baiyi.opscloud.domain.vo.serverChange;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/30 9:20 上午
 * @Version 1.0
 */
public class ServerChangeTaskVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerChangeTask {

        private List<ServerChangeTaskFlow> taskFlows;
        private Integer id;
        @ApiModelProperty(value = "任务uuid")
        private String taskId;
        @ApiModelProperty(value = "服务器id", example = "1")
        private Integer serverId;
        @ApiModelProperty(value = "服务器组id", example = "1")
        private Integer serverGroupId;
        @ApiModelProperty(value = "变更类型")
        private String changeType;
        @ApiModelProperty(value = "变更数量", example = "1")
        private Integer changeNumber;
        @ApiModelProperty(value = "当前流程id", example = "1")
        private Integer taskFlowId;
        @ApiModelProperty(value = "当前流程名称")
        private String taskFlowName;
        @ApiModelProperty(value = "任务执行返回值, 0:成功 !0:错误", example = "0")
        private Integer resultCode;
        @ApiModelProperty(value = "任务执行信息")
        private String resultMsg;
        @ApiModelProperty(value = "任务执行状态, 1:执行中 0:结束", example = "1")
        private Integer taskStatus;
        @ApiModelProperty(value = "任务开始时间")
        private Date startTime;
        @ApiModelProperty(value = "任务结束时间")
        private Date endTime;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerChangeTaskFlow {
        private Integer id;
        @ApiModelProperty(value = "任务uuid")
        private String taskId;
        @ApiModelProperty(value = "父流程id", example = "0")
        private Integer flowParentId;
        @ApiModelProperty(value = "当前流程名称")
        private String taskFlowName;
        private Integer resultCode;
        private String resultMsg;
        private Integer taskStatus;
        @ApiModelProperty(value = "外部id（脚本）",example = "0")
        private Integer externalId;
        @ApiModelProperty(value = "外部类型（脚本）")
        private String externalType;
        private Date startTime;
        private Date endTime;
        @ApiModelProperty(value = "任务详情")
        private String taskDetail;
    }
}
