package com.baiyi.opscloud.event.customer.impl;

import com.baiyi.opscloud.datasource.manager.DsAccountManager;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.event.NoticeEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/18 11:19 上午
 * @Version 1.0
 */
@Component
public class UserEventCustomer extends AbstractEventConsumer<User> {

    @Resource
    private DsAccountManager dsAccountManager;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.USER.name();
    }

    @Override
    protected void onCreateMessage(NoticeEvent noticeEvent) {
        User eventData = toEventData(noticeEvent.getMessage());
        dsAccountManager.create(eventData);
    }

    @Override
    protected void onUpdateMessage(NoticeEvent noticeEvent) {
        User eventData = toEventData(noticeEvent.getMessage());
        dsAccountManager.update(eventData);
    }

    @Override
    protected void onDeleteMessage(NoticeEvent noticeEvent) {
        User eventData = toEventData(noticeEvent.getMessage());
        dsAccountManager.delete(eventData);
    }

}
