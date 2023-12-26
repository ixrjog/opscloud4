package com.baiyi.opscloud.domain.param.leo.request;

import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2022/11/23 16:48
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubscribeLeoJobRequestParam extends SuperPageParam implements ILeoRequestParam {

    @Schema(description = "应用ID")
    private Integer applicationId;

    @Schema(description = "查询名称")
    private String queryName;

    @Schema(description = "环境类型")
    private Integer envType;

    @Schema(description = "构建类型")
    private String buildType;

    private final Boolean extend = true;

    @Override
    public String getMessageType() {
        return LeoRequestType.SUBSCRIBE_LEO_JOB.name();
    }

}