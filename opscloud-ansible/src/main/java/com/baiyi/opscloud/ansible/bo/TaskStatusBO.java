package com.baiyi.opscloud.ansible.bo;

import com.baiyi.opscloud.common.base.ServerTaskStatus;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/19 3:29 下午
 * @Version 1.0
 */
@Builder
@Data
public class TaskStatusBO {

    @Builder.Default
    private Integer finalized = 1;
    private Integer stopType;
    @Builder.Default
    private Integer exitValue = 1; // 错误
    @Builder.Default
    private String taskStatus = ServerTaskStatus.FINALIZED.getStatus();

    private String tastResult;

}
