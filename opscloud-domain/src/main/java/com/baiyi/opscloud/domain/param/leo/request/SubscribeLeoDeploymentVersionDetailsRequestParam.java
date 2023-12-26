package com.baiyi.opscloud.domain.param.leo.request;

import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2023/2/22 18:15
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubscribeLeoDeploymentVersionDetailsRequestParam extends SuperPageParam implements ILeoRequestParam {

    private Integer applicationId;

    private Integer envType;

    @Override
    public String getMessageType() {
        return LeoRequestType.SUBSCRIBE_LEO_DEPLOYMENT_VERSION_DETAILS.name();
    }

}