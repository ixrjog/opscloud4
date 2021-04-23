package com.baiyi.opscloud.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.bo.workorder.WorkorderTicketEntryBO;
import com.baiyi.opscloud.common.base.WorkorderKey;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.auth.RoleVO;
import com.baiyi.opscloud.domain.vo.cloud.AliyunAccountVO;
import com.baiyi.opscloud.domain.vo.cloud.AliyunRAMVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.opscloud.factory.ticket.entry.AuthRoleEntry;
import com.baiyi.opscloud.factory.ticket.entry.RAMPolicyEntry;
import com.baiyi.opscloud.factory.ticket.entry.ServerGroupEntry;
import com.baiyi.opscloud.factory.ticket.entry.UserGroupEntry;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/28 12:53 下午
 * @Version 1.0
 */
public class WorkorderTicketEntryBuilder {

    public static OcWorkorderTicketEntry build(WorkorderTicketEntryParam.TicketEntry ticketEntry, String name) {
        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketEntry.getTicketId())
                .name(name)
                .entryKey(ticketEntry.getEntryKey())
                .entryDetail(JSON.toJSONString(ticketEntry.getTicketEntry()))
                .build();
        return covert(workorderTicketEntryBO);
    }

    public static WorkorderTicketEntryVO.Entry build(int ticketId, OcServerGroup ocServerGroup) {
        ServerGroupEntry entry = ServerGroupEntry.builder()
                .serverGroup(BeanCopierUtils.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class))
                .build();

        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketId)
                .name(ocServerGroup.getName())
                .businessId(ocServerGroup.getId())
                .entryKey(WorkorderKey.SERVER_GROUP.getKey())
                .comment(ocServerGroup.getComment())
                .ticketEntry(entry)
                .entryDetail(JSON.toJSONString(entry))
                .build();
        return covertVO(workorderTicketEntryBO);
    }

    public static WorkorderTicketEntryVO.Entry build(int ticketId, OcAuthRole ocAuthRole) {
        AuthRoleEntry entry = AuthRoleEntry.builder()
                .role(BeanCopierUtils.copyProperties(ocAuthRole, RoleVO.Role.class))
                .build();

        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketId)
                .name(ocAuthRole.getRoleName())
                .businessId(ocAuthRole.getId())
                .entryKey(WorkorderKey.AUTH_ROLE.getKey())
                .comment(ocAuthRole.getComment())
                .ticketEntry(entry)
                .entryDetail(JSON.toJSONString(entry))
                .build();
        return covertVO(workorderTicketEntryBO);
    }

    public static WorkorderTicketEntryVO.Entry build(int ticketId, OcUserGroup ocUserGroup) {
        UserGroupEntry entry = UserGroupEntry.builder()
                .userGroup(BeanCopierUtils.copyProperties(ocUserGroup, UserGroupVO.UserGroup.class))
                .build();
        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketId)
                .name(ocUserGroup.getName())
                .businessId(ocUserGroup.getId())
                .entryKey(WorkorderKey.USER_GROUP.getKey())
                .comment(ocUserGroup.getComment())
                .ticketEntry(entry)
                .entryDetail(JSON.toJSONString(entry))
                .build();
        return covertVO(workorderTicketEntryBO);
    }


    public static WorkorderTicketEntryVO.Entry build(int ticketId, OcAliyunRamPolicy ocAliyunRamPolicy, List<AliyunAccountVO.AliyunAccount> aliyunAccounts) {
        AliyunRAMVO.RAMPolicy ramPolicy = BeanCopierUtils.copyProperties(ocAliyunRamPolicy, AliyunRAMVO.RAMPolicy.class);
        ramPolicy.setCreateDate(null);
        ramPolicy.setUpdateDate(null);
        aliyunAccounts.forEach(e->{
            if(e.getUid().equals(ramPolicy.getAccountUid()))
                ramPolicy.setAccountName(e.getName());
        });
        RAMPolicyEntry entry = RAMPolicyEntry.builder()
                .ramPolicy(ramPolicy)
                .build();
        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketId)
                .name(ocAliyunRamPolicy.getPolicyName())
                .businessId(ocAliyunRamPolicy.getId())
                .entryKey(WorkorderKey.RAM_POLICY.getKey())
                .comment(ocAliyunRamPolicy.getDescription())
                .ticketEntry(entry)
                .entryDetail(JSON.toJSONString(entry))
                .build();
        return covertVO(workorderTicketEntryBO);
    }

    private static WorkorderTicketEntryVO.Entry covertVO(WorkorderTicketEntryBO workorderTicketEntryBO) {
        return BeanCopierUtils.copyProperties(workorderTicketEntryBO, WorkorderTicketEntryVO.Entry.class);
    }

    private static OcWorkorderTicketEntry covert(WorkorderTicketEntryBO workorderTicketEntryBO) {
        return BeanCopierUtils.copyProperties(workorderTicketEntryBO, OcWorkorderTicketEntry.class);
    }


}
