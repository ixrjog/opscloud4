package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.datasource.manager.DsAccountGroupManager;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.common.event.NoticeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/11/1 9:52 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserGroupEventConsumer extends AbstractEventConsumer<UserGroup> {

    private final DsAccountGroupManager dsAccountGroupManager;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.USERGROUP.name();
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<UserGroup> noticeEvent) {
        UserGroup eventData = toEventData(noticeEvent.getMessage());
        dsAccountGroupManager.create(eventData);
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<UserGroup> noticeEvent) {
        UserGroup eventData = toEventData(noticeEvent.getMessage());
        dsAccountGroupManager.update(eventData);
    }

    @Override
    protected void onDeletedMessage(NoticeEvent<UserGroup> noticeEvent) {
        UserGroup eventData = toEventData(noticeEvent.getMessage());
        dsAccountGroupManager.delete(eventData);
    }

}
