package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:22 上午
 * @Version 1.0
 */
@Data
@Builder
public class CloudInstanceTaskBO {

    private Integer id;
    private Integer cloudType;
    private Integer templateId;
    private Integer userId;
    private String regionId;
    private Integer createSize;
    private String taskPhase;
    private String taskStatus;
    private String errorMsg;
    @Builder.Default
    private Integer taskType = 0;
    private Date createTime;
    private Date updateTime;
    private String userDetail;
    private String createInstance;
    private String comment;
}
