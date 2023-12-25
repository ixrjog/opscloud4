package com.baiyi.opscloud.event.listener;

import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.event.consumer.EventConsumerFactory;
import com.baiyi.opscloud.event.consumer.IEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/17 4:29 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class EventListener<T> implements ApplicationListener<NoticeEvent<T>> {

    @SuppressWarnings("unchecked")
    @Override
    @Async
    public void onApplicationEvent(NoticeEvent<T> noticeEvent) {
        log.debug("监听事件: eventType={}, action={}", noticeEvent.getMessage().getEventType(), noticeEvent.getMessage().getAction());
        IEventConsumer<T> consumer = EventConsumerFactory.getConsumer(noticeEvent.getMessage().getEventType());
        if (consumer != null) {
            consumer.onMessage(noticeEvent);
        }
    }

}