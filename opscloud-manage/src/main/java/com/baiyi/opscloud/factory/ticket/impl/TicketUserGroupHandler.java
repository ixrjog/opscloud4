package com.baiyi.opscloud.factory.ticket.impl;

import com.baiyi.opscloud.common.base.WorkorderKey;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketEntryVO;
import com.baiyi.opscloud.facade.UserFacade;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.entry.ITicketEntry;
import com.baiyi.opscloud.factory.ticket.entry.ServerGroupEntry;
import com.baiyi.opscloud.factory.ticket.entry.UserGroupEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/27 6:33 下午
 * @Version 1.0
 */
@Slf4j
@Component("TicketUserGroupExecutor")
public class TicketUserGroupHandler<T> extends BaseTicketHandler<T> implements ITicketHandler {

    @Resource
    private UserFacade userFacade;

    @Override
    public String getKey() {
        return WorkorderKey.USER_GROUP.getKey();
    }

    @Override
    protected String acqWorkorderKey() {
        return WorkorderKey.USER_GROUP.getKey();
    }

    @Override
    protected ITicketEntry acqITicketEntry(Object  ticketEntry) {
        UserGroupEntry entry = new ObjectMapper().convertValue(ticketEntry, UserGroupEntry.class);
        return entry;
    }

    @Override
    protected T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        ServerGroupEntry entry = gson.fromJson(ocWorkorderTicketEntry.getEntryDetail(), ServerGroupEntry.class);
        return (T) entry;
    }

    @Override
    protected void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry) {
        UserGroupEntry userGroupEntry = (UserGroupEntry) entry;
        UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission = new UserBusinessGroupParam.UserUserGroupPermission();

        userUserGroupPermission.setUserId(getUser(ocWorkorderTicketEntry.getWorkorderTicketId()).getId());
        userUserGroupPermission.setUserGroupId(userGroupEntry.getUserGroup().getId());

        BusinessWrapper<Boolean> wrapper = userFacade.grantUserUserGroup(userUserGroupPermission);
        saveTicketEntry(ocWorkorderTicketEntry, wrapper);
    }

    @Override
    protected BusinessWrapper<Boolean> updateTicketEntry(OcWorkorderTicketEntryVO.Entry entry) {
        return BusinessWrapper.SUCCESS;
    }
}
