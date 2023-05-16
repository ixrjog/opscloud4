package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.datasource.manager.DsAccountGroupManager;
import com.baiyi.opscloud.datasource.manager.DsServerGroupManager;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.event.consumer.ApplicationManager;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/20 9:39 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPermissionEventConsumer extends AbstractEventConsumer<UserPermission> {

    private final DsServerGroupManager dsServerGroupManager;

    private final DsAccountGroupManager dsAccountGroupManager;

    private final UserService userService;

    private final ApplicationManager applicationManager;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.USER_PERMISSION.name();
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<UserPermission> noticeEvent) {
        UserPermission eventData = toEventData(noticeEvent.getMessage());
        if (eventData.getBusinessType() == BusinessTypeEnum.USERGROUP.getType()) {
            dsAccountGroupManager.grant(userService.getById(eventData.getUserId()), eventData);
            return;
        }
        if (eventData.getBusinessType() == BusinessTypeEnum.SERVERGROUP.getType()) {
            dsServerGroupManager.grant(userService.getById(eventData.getUserId()), eventData);
            return;
        }
        if (eventData.getBusinessType() == BusinessTypeEnum.APPLICATION.getType()) {
            applicationManager.create(eventData);
            return;
        }
    }

    @Override
    protected void onDeletedMessage(NoticeEvent<UserPermission> noticeEvent) {
        UserPermission eventData = toEventData(noticeEvent.getMessage());
        if (eventData.getBusinessType() == BusinessTypeEnum.USERGROUP.getType()) {
            dsAccountGroupManager.revoke(userService.getById(eventData.getUserId()), eventData);
        }
        if (eventData.getBusinessType() == BusinessTypeEnum.SERVERGROUP.getType()) {
            dsServerGroupManager.revoke(userService.getById(eventData.getUserId()), eventData);
        }
    }

}

