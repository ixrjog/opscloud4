package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.datasource.manager.DsAccountManager;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.common.event.NoticeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/18 11:19 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserEventConsumer extends AbstractEventConsumer<User> {

    private final DsAccountManager dsAccountManager;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.USER.name();
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<User> noticeEvent) {
        User eventData = toEventData(noticeEvent.getMessage());
        dsAccountManager.create(eventData);
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<User> noticeEvent) {
        User eventData = toEventData(noticeEvent.getMessage());
        dsAccountManager.update(eventData);
    }

    @Override
    protected void onDeletedMessage(NoticeEvent<User> noticeEvent) {
        User eventData = toEventData(noticeEvent.getMessage());
        dsAccountManager.delete(eventData);
    }

}
