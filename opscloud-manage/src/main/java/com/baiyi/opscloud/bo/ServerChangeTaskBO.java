package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/27 4:42 下午
 * @Version 1.0
 */
@Data
@Builder
public class ServerChangeTaskBO {

    private Integer id;
    private String taskId;
    private Integer serverId;
    private Integer serverGroupId;
    private String changeType;
    @Builder.Default
    private Integer changeNumber = 1;
    private Integer taskFlowId;
    private String taskFlowName;
    @Builder.Default
    private Integer resultCode = 0;
    private String resultMsg;
    @Builder.Default
    private Integer taskStatus = 1;
    @Builder.Default
    private Date startTime = new Date();
    private Date endTime;

}
