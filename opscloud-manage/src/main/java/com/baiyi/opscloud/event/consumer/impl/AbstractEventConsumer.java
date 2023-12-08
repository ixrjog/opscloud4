package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.event.IEvent;
import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.common.helper.topic.TopicHelper;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.event.consumer.EventConsumerFactory;
import com.baiyi.opscloud.event.consumer.IEventConsumer;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author baiyi
 * @Date 2021/8/17 7:02 下午
 * @Version 1.0
 */
public abstract class AbstractEventConsumer<T> implements IEventConsumer<T>, InitializingBean {

    @Resource
    protected TopicHelper topicHelper;

    @Override
    @Async
    public void onMessage(NoticeEvent<T> noticeEvent) {
        // 预处理
        preHandle(noticeEvent);
        String action = noticeEvent.getMessage().getAction();
        messageRoute(noticeEvent, action);
        // 后处理
        postHandle(noticeEvent);
    }

    private void messageRoute(NoticeEvent<T> noticeEvent, String action) {
        if (EventActionTypeEnum.CREATE.name().equals(action)) {
            onCreatedMessage(noticeEvent);
            return;
        }
        if (EventActionTypeEnum.UPDATE.name().equals(action)) {
            onUpdatedMessage(noticeEvent);
            return;
        }
        if (EventActionTypeEnum.DELETE.name().equals(action)) {
            onDeletedMessage(noticeEvent);
        }
    }

    /**
     * chuang
     *
     * @param noticeEvent
     */
    protected void onCreatedMessage(NoticeEvent<T> noticeEvent) {
    }

    protected void onUpdatedMessage(NoticeEvent<T> noticeEvent) {
    }

    protected void onDeletedMessage(NoticeEvent<T> noticeEvent) {
    }

    /**
     * 预处理
     *
     * @param noticeEvent
     */
    protected void preHandle(NoticeEvent<T> noticeEvent) {
    }

    /**
     * 后处理
     *
     * @param noticeEvent
     */
    protected void postHandle(NoticeEvent<T> noticeEvent) {
    }

    protected T toEventData(IEvent<T> event) {
        return event.getBody();
    }

    @Override
    public void afterPropertiesSet() {
        EventConsumerFactory.register(this);
    }
}
