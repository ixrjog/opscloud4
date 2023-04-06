package com.baiyi.opscloud.alert.notify.impl;

import com.baiyi.opscloud.alert.notify.NotifyMediaEnum;
import com.baiyi.opscloud.domain.alert.AlertContext;
import com.baiyi.opscloud.domain.alert.AlertNotifyMedia;
import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.datasource.message.notice.DingtalkSendHelper;
import com.baiyi.opscloud.domain.generator.opscloud.AlertNotifyHistory;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @Resource
    private MessageTemplateService messageTemplateService;

    private static final String CONSUL_ALERT = "CONSUL_ALERT";

    @Override
    public void doNotify(AlertNotifyMedia media, AlertContext context) {
        if (StringUtils.isNotBlank(media.getDingtalkToken())) {
            Map<String, Object> contentMap = Maps.newHashMap();
            contentMap.put("service", context.getService());
            contentMap.put("check", context.getCheck());
            contentMap.put("message", context.getMessage());
            contentMap.put("value", context.getValue());
            contentMap.put("alertTime", context.getAlertTime());
            contentMap.put("users", media.getUsers());
            contentMap.put("metadata", context.getMetadata());
            try {
                MessageTemplate messageTemplate = messageTemplateService.getByUniqueKey(CONSUL_ALERT, "DINGTALK_APP", "markdown");
                if (messageTemplate == null) {
                    return;
                }
                String msg = BeetlUtil.renderTemplate(messageTemplate.getMsgTemplate(), contentMap);
                dingtalkSendHelper.send(media.getDingtalkToken(), msg);
                saveAlertNotify(context, buildAlertNotifyHistoryList());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private List<AlertNotifyHistory> buildAlertNotifyHistoryList() {
        return Lists.newArrayList(buildAlertNotifyHistory());
    }

    @Override
    public String getKey() {
        return NotifyMediaEnum.DINGTALK.name();
    }

}
