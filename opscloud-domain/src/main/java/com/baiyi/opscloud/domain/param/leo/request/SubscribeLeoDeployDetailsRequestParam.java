package com.baiyi.opscloud.domain.param.leo.request;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/12/6 19:35
 * @Version 1.0
 */
@Data
public class SubscribeLeoDeployDetailsRequestParam implements ILeoRequestParam, ILoginMessage {

    private String token;

    private Integer deployId;

    @Override
    public String getMessageType() {
        return LeoRequestType.SUBSCRIBE_LEO_DEPLOY_DETAILS.name();
    }

}