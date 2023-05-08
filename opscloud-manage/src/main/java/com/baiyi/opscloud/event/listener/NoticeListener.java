package com.baiyi.opscloud.event.listener;

import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.event.consumer.EventConsumerFactory;
import com.baiyi.opscloud.event.consumer.IEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;

/**
 * @Author baiyi
 * @Date 2021/8/17 4:29 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class NoticeListener<T> implements ApplicationListener<NoticeEvent<T>> {

    @Override
    @Async(value = CORE)
    public void onApplicationEvent(NoticeEvent<T> noticeEvent) {
        log.info("监听事件: eventType={}, action={}", noticeEvent.getMessage().getEventType(), noticeEvent.getMessage().getAction());
        IEventConsumer iEventConsumer = EventConsumerFactory.getConsumer(noticeEvent.getMessage().getEventType());
        if (iEventConsumer == null) {
            log.debug("当前事件没有Consumer");
            return;
        }
        iEventConsumer.onMessage(noticeEvent);
    }

}
