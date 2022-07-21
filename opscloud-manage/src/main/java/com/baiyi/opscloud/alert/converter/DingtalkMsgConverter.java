package com.baiyi.opscloud.alert.converter;

import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import com.baiyi.opscloud.datasource.message.DingtalkMsg;
import com.google.common.base.Joiner;

/**
 * @Author 修远
 * @Date 2022/7/21 10:27 PM
 * @Since 1.0
 */
public class DingtalkMsgConverter {

    public static DingtalkMsg.Msg converter(AlertNotifyMedia media, AlertContext context) {
        String text = Joiner.on(" ").join(context.getService(), context.getMessage());
        DingtalkMsg.Markdown markdown = DingtalkMsg.Markdown.builder()
                .title("Consul 告警")
                .text(text)
                .build();
        return DingtalkMsg.Msg.builder()
                .markdown(markdown)
                .build();
    }
}
