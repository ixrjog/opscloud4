package com.baiyi.opscloud.ansible.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:03 下午
 * @Version 1.0
 */
@Data
@Builder
public class ServerTaskBO {

    private Integer id;

    private String executorParam;

    private Integer userId;

    @Builder.Default
    private Integer taskType = 0;

    private String comment;

    private Integer taskSize;

    @Builder.Default
    private Integer finalized = 0;

    @Builder.Default
    private Integer stopType = 0;

    @Builder.Default
    private Integer systemType = 0; // 0 用户执行  1 变更管理  2 日志清理

    private Integer exitValue;

    private String taskStatus;

    private Date createTime;

    private Date updateTime;

    private String userDetail;

    private String serverTargetDetail;

}
