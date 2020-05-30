package com.baiyi.opscloud.domain.vo.serverChange;

import io.swagger.annotations.ApiModel;
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
        private String taskId;
        private Integer serverId;
        private Integer serverGroupId;
        private String changeType;
        private Integer changeNumber;
        private Integer taskFlowId;
        private String taskFlowName;
        private Integer resultCode;
        private String resultMsg;
        private Integer taskStatus;
        private Date startTime;
        private Date endTime;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerChangeTaskFlow {

        private Integer id;
        private String taskId;
        private Integer flowParentId;
        private String taskFlowName;
        private Integer resultCode;
        private String resultMsg;
        private Integer taskStatus;
        private Integer externalId;
        private String externalType;
        private Date startTime;
        private Date endTime;
        private String taskDetail;
    }
}
