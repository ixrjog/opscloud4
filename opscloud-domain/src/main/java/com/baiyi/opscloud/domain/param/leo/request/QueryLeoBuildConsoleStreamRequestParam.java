package com.baiyi.opscloud.domain.param.leo.request;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/11/29 17:51
 * @Version 1.0
 */
@Data
public class QueryLeoBuildConsoleStreamRequestParam implements ILeoRequestParam, ILoginMessage {

    private String token;

    private Integer buildId;

    @Override
    public String getMessageType() {
        return LeoRequestType.QUERY_LEO_BUILD_CONSOLE_STREAM.name();
    }

}