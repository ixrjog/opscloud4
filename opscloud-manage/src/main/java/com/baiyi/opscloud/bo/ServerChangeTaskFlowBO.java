package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/28 2:02 下午
 * @Version 1.0
 */
@Data
@Builder
public class ServerChangeTaskFlowBO {

    private Integer id;
    private String taskId;
    @Builder.Default
    private Integer flowParentId = 0;
    private String taskFlowName;
    private Integer resultCode;
    private String resultMsg;
    @Builder.Default
    private Integer taskStatus = -1; // 1:执行中 0:结束
    private Integer externalId;
    private String externalType;
    private Date startTime;
    private Date endTime;
    private String taskDetail;

}
