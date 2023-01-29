package com.baiyi.opscloud.facade.message;

import com.baiyi.opscloud.datasource.message.LXHLMessageResponse;
import com.baiyi.opscloud.domain.param.message.MessageParam;

/**
 * @Author 修远
 * @Date 2022/9/7 8:03 PM
 * @Since 1.0
 */
public interface MessageFacade {

    LXHLMessageResponse.SendMessage sendMessage(MessageParam.SendMessage param);

    LXHLMessageResponse.SendMessage sendMessage(String media, String mobiles, String platform, String platformToken, MessageParam.GrafanaMessage parma);
}
