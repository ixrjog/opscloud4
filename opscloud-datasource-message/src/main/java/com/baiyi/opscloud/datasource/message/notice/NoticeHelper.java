package com.baiyi.opscloud.datasource.message.notice;

import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.datasource.message.consumer.IMessageConsumer;
import com.baiyi.opscloud.datasource.message.consumer.MessageConsumerFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.MessageTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/12/2 10:52 AM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeHelper {

    private final MessageTemplateService messageTemplateService;

    private static final String MSG_TYPE = "markdown";

    public void sendMessage(User user, String msgKey, List<DatasourceInstance> instances) {
        for (DatasourceInstance instance : instances) {
            MessageTemplate mt = getTemplate(msgKey, instance);
            if (mt == null) {
                continue;
            }
            Map<String, Object> contentMap = Maps.newHashMap();
            contentMap.put("username", user.getUsername());
            if (!StringUtils.isEmpty(user.getPassword())) {
                contentMap.put("password", user.getPassword());
            }
            try {
                String text = BeetlUtil.renderTemplate(mt.getMsgTemplate(), contentMap);
                IMessageConsumer iMessageCustomer =
                        MessageConsumerFactory.getConsumerByInstanceType(instance.getInstanceType());
                if (iMessageCustomer == null) {
                    continue;
                }
                iMessageCustomer.send(instance, user, mt, text);
                return;
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void sendMessage(User user, String msgKey, List<DatasourceInstance> instances, INoticeMessage iNoticeMessage) {
        for (DatasourceInstance instance : instances) {
            MessageTemplate mt = getTemplate(msgKey, instance);
            if (mt == null) {
                continue;
            }
            Map<String, Object> contentMap = iNoticeMessage.toContentMap();
            this.send(instance, user, mt, contentMap);
        }
    }

    private void send(DatasourceInstance instance, User user, MessageTemplate mt, Map<String, Object> contentMap) {
        try {
            // 渲染模板
            String text = BeetlUtil.renderTemplate(mt.getMsgTemplate(), contentMap);
            IMessageConsumer iMessageCustomer =
                    MessageConsumerFactory.getConsumerByInstanceType(instance.getInstanceType());
            if (iMessageCustomer == null) {
                return;
            }
            iMessageCustomer.send(instance, user, mt, text);
            log.info("发送用户消息: instanceName={}, username={}", instance.getInstanceName(), user.getUsername());
        } catch (IOException e) {
            log.error("发送用户消息错误: instanceName={}, username={}", instance.getInstanceName(), user.getUsername());
        }
    }

    public MessageTemplate getTemplate(String msgKey, DatasourceInstance datasourceInstance) {
        return messageTemplateService.getByUniqueKey(msgKey, datasourceInstance.getInstanceType(), MSG_TYPE);
    }

}