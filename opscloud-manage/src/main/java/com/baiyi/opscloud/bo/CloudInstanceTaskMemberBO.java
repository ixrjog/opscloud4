package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/30 5:57 下午
 * @Version 1.0
 */
@Data
@Builder
public class CloudInstanceTaskMemberBO {

    private Integer id;
    private Integer taskId;
    private String instanceId;
    private Integer seq;
    private String privateIp;
    private String publicIp;
    private String regionId;
    private String zoneId;
    private String hostname;
    private String taskStatus;
    private String taskPhase;
    private Integer retryCount;
    private Integer errorCode;
    private Date createTime;
    private Date updateTime;
    private String detail;
    private String errorMsg;
}
