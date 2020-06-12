package com.baiyi.opscloud.ansible.bo;

import com.baiyi.opscloud.common.base.ServerTaskStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/8 2:04 下午
 * @Version 1.0
 */
@Data
@Builder
public class ServerTaskMemberBO {
    private Integer id;
    private Integer taskId;
    private String hostPattern;
    private String manageIp;
    private String comment;
    @Builder.Default
    private Integer finalized = 0;
    @Builder.Default
    private Integer stopType = 0;
    private Integer exitValue;
    @Builder.Default
    private String taskStatus= ServerTaskStatus.QUEUE.getStatus();
    private Integer serverId;
    private Integer envType;

    private String taskResult ;
    private Date createTime;
    private Date updateTime;
    @Builder.Default
    private String outputMsg = "";
    @Builder.Default
    private String errorMsg = "";
}
