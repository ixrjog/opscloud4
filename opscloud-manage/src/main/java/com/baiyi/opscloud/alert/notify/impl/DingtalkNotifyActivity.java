package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.converter.DingtalkMsgConverter;
import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.common.alert.AlertContext;
import com.baiyi.opscloud.common.alert.AlertNotifyMedia;
import com.baiyi.opscloud.datasource.message.DingtalkMsg;
import com.baiyi.opscloud.datasource.message.notice.DingtalkSendHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author 修远
 * @Date 2022/7/21 12:17 AM
 * @Since 1.0
 */

@Slf4j
@Component
public class DingtalkNotifyActivity extends AbstractNotifyActivity {

    @Resource
    private DingtalkSendHelper dingtalkSendHelper;

    @Override
    public void doNotify(AlertNotifyMedia media, AlertContext context) {
        if (StringUtils.isNotBlank(media.getDingtalkToken())) {
            DingtalkMsg.Msg msg = DingtalkMsgConverter.converter(media, context);
            dingtalkSendHelper.send(media.getDingtalkToken(), msg);
            return;
        }
        log.error("告警失败：钉钉Token为空" + context.getSource());
    }

    @Override
    public String getKey() {
        return NotifyMediaEnum.DINGTALK.name();
    }
}
