package com.baiyi.opscloud.datasource.message.driver;

import com.baiyi.opscloud.common.datasource.LXHLConfig;
import com.baiyi.opscloud.datasource.lxhl.driver.MessageServiceDriver;
import com.baiyi.opscloud.datasource.message.LXHLMessageResponse;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2022/9/1 3:26 PM
 * @Since 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class LXHLMessageDriver {

    private final MessageServiceDriver messageServiceDriver;

    public LXHLMessageResponse.SendMessage sendMessage(LXHLConfig.Account account, String mobile, String content, String signName) {
        final String signContent = preInvokeSign(signName, content);
        String result = messageServiceDriver.sendMessage(account, mobile, signContent);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        String[] response = result.split(",");
        LXHLMessageResponse.SendMessage sendMessage = LXHLMessageResponse.SendMessage.builder()
                .code(response[0])
                .build();
        sendMessage.setRequestId(response.length == 1 ? null : response[1]);
        return sendMessage;
    }

    private String preInvokeSign(String signName, String content) {
        return Joiner.on("").join("【", signName, "】", content);
    }

}