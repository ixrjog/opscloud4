package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.helper.TopicHelper;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.common.event.IEvent;
import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.event.consumer.EventConsumerFactory;
import com.baiyi.opscloud.event.consumer.IEventConsumer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;

import static com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration.TaskPools.CORE;

/**
 * @Author baiyi
 * @Date 2021/8/17 7:02 下午
 * @Version 1.0
 */
public abstract class AbstractEventConsumer<T> implements IEventConsumer, InitializingBean {

    @Resource
    protected TopicHelper topicHelper;

    @Override
    @Async(value = CORE)
    public void onMessage(NoticeEvent noticeEvent) {
        preHandle(noticeEvent); // 预处理
        String action = noticeEvent.getMessage().getAction();
        messageRoute(noticeEvent, action);
        postHandle(noticeEvent); // 后处理
    }

    private void messageRoute(NoticeEvent noticeEvent, String action) {
        if (EventActionTypeEnum.CREATE.name().equals(action)) {
            onCreateMessage(noticeEvent);
            return;
        }
        if (EventActionTypeEnum.UPDATE.name().equals(action)) {
            onUpdateMessage(noticeEvent);
            return;
        }
        if (EventActionTypeEnum.DELETE.name().equals(action)) {
            onDeleteMessage(noticeEvent);
        }
    }

    protected void onCreateMessage(NoticeEvent noticeEvent) {
    }

    protected void onUpdateMessage(NoticeEvent noticeEvent) {
    }

    protected void onDeleteMessage(NoticeEvent noticeEvent) {
    }

    /**
     * 预处理
     *
     * @param noticeEvent
     */
    protected void preHandle(NoticeEvent noticeEvent) {
    }

    /**
     * 后处理
     *
     * @param noticeEvent
     */
    protected void postHandle(NoticeEvent noticeEvent) {
    }

    protected T toEventData(IEvent<T> event) {
        return event.getBody();
    }

    @Override
    public void afterPropertiesSet() {
        EventConsumerFactory.register(this);
    }
}
