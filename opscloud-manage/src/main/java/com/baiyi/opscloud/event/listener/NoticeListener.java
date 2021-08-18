package com.baiyi.opscloud.event.listener;

import com.baiyi.opscloud.event.NoticeEvent;
import com.baiyi.opscloud.event.customer.EventConsumerFactory;
import com.baiyi.opscloud.event.customer.IEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/17 4:29 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class NoticeListener implements ApplicationListener<NoticeEvent> {

    @Override
    public void onApplicationEvent(NoticeEvent noticeEvent) {
        log.info("监听事件 : eventType = {} , action = {}", noticeEvent.getMessage().getEventType(), noticeEvent.getMessage().getAction());
        IEventConsumer iEventConsumer = EventConsumerFactory.getConsumer(noticeEvent.getMessage().getEventType());
        if (iEventConsumer == null) {
            log.info("当前事件没有Consumer");
            return;
        }
        iEventConsumer.onMessage(noticeEvent);
    }
}
