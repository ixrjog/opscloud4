package com.baiyi.opscloud.factory.ticket.impl.handler;

import com.baiyi.opscloud.aliyun.ram.handler.AliyunRAMUserPolicyHandler;
import com.baiyi.opscloud.common.base.WorkorderKey;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.factory.ticket.ITicketHandler;
import com.baiyi.opscloud.factory.ticket.entry.ITicketEntry;
import com.baiyi.opscloud.factory.ticket.entry.RAMPolicyEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/11 3:06 下午
 * @Version 1.0
 */
@Slf4j
@Component("TicketRAMPolicyExecutor")
public class TicketRAMPolicyHandler<T> extends BaseTicketHandler<T> implements ITicketHandler {

    @Resource
    private AliyunRAMUserPolicyHandler aliyunRAMUserPolicyHandler;

    @Override
    public String getKey() {
        return WorkorderKey.RAM_POLICY.getKey();
    }

    @Override
    protected String acqWorkorderKey() {
        return WorkorderKey.AUTH_ROLE.getKey();
    }

    @Override
    protected ITicketEntry acqITicketEntry(Object ticketEntry) {
        return new ObjectMapper().convertValue(ticketEntry, RAMPolicyEntry.class);
    }

    @Override
    protected T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException {
        return (T) new GsonBuilder().create().fromJson(ocWorkorderTicketEntry.getEntryDetail(), RAMPolicyEntry.class);
    }

    @Override
    protected void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry) {
        RAMPolicyEntry ramPolicyEntry = (RAMPolicyEntry) entry;
        // 校验RAM账户

        // 授权

        // 更新数据

       // aliyunRAMUserPolicyHandler.attachPolicyToUser()

//        UserRoleVO.UserRole userRole = new UserRoleVO.UserRole();
//        userRole.setRoleId(authRoleEntry.getRole().getId());
//        userRole.setUsername(getUser(ocWorkorderTicketEntry.getWorkorderTicketId()).getUsername());
//
//        authFacade.addUserRole(userRole);
        saveTicketEntry(ocWorkorderTicketEntry, BusinessWrapper.SUCCESS);
    }

    @Override
    protected BusinessWrapper<Boolean> updateTicketEntry(WorkorderTicketEntryVO.Entry entry) {
        return BusinessWrapper.SUCCESS;
    }

}