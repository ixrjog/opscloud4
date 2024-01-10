package com.baiyi.opscloud.domain.param.leo.request;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/11/23 15:36
 * @Version 1.0
 */
@Data
@Builder
public class LoginLeoRequestParam implements ILeoRequestParam, ILoginMessage {

    private String token;

    @Override
    public String getMessageType() {
        return LeoRequestType.LOGIN.name();
    }

}