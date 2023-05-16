package com.baiyi.opscloud.datasource.ansible;

import com.baiyi.opscloud.common.base.ServerTaskStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/9/22 5:43 下午
 * @Version 1.0
 */
@Builder
@Data
public class TaskStatus {

    @Builder.Default
    private Boolean finalized = true;
    private Integer stopType;

    @Schema(description = "错误")
    @Builder.Default
    private Integer exitValue = 1;

    @Builder.Default
    private String taskStatus = ServerTaskStatusEnum.FINALIZED.name();

    private String taskResult;

}
