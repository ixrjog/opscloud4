package com.baiyi.opscloud.workorder.query.impl.extended;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.user.UserGroupService;
import com.baiyi.opscloud.workorder.processor.ITicketProcessor;
import com.baiyi.opscloud.workorder.processor.factory.WorkOrderTicketProcessorFactory;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractUserGroupPermissionExtendedAbstractUserPermission;
import com.baiyi.opscloud.workorder.query.impl.base.BaseTicketEntryQuery;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;

import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2022/1/11 5:19 PM
 * @Version 1.0
 */
public abstract class BaseUserGroupExtendedTicketEntryQuery extends BaseTicketEntryQuery<UserGroup> {

    @Resource
    private UserGroupService userGroupService;

    @Override
    protected List<UserGroup> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        List<UserGroup> entries = Lists.newArrayList();
        ITicketProcessor<?> ticketProcessor = WorkOrderTicketProcessorFactory.getByKey(getKey());
        if (ticketProcessor == null) {
            return entries;
        }
        if (ticketProcessor instanceof AbstractUserGroupPermissionExtendedAbstractUserPermission) {
            Set<String> groupNames = ((AbstractUserGroupPermissionExtendedAbstractUserPermission) ticketProcessor).getGroupNames();
            entries.addAll(groupNames.stream()
                    .map(this::getEntryByName)
                    // 过滤不允许工单申请的用户组（角色）
                    .filter(UserGroup::getAllowOrder)
                    .toList()
            );
        }
        return entries;
    }

    private UserGroup getEntryByName(String name) {
        return userGroupService.getByName(name);
    }

    @Override
    protected WorkOrderTicketVO.Entry<UserGroup> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, UserGroup entry) {
        return WorkOrderTicketVO.Entry.<UserGroup>builder()
                .workOrderTicketId(entryQuery.getWorkOrderTicketId())
                .name(entry.getName())
                .entryKey(entry.getName())
                .businessType(BusinessTypeEnum.USERGROUP.getType())
                .businessId(entry.getId())
                .content(JSONUtil.writeValueAsString(entry))
                .comment(entry.getComment())
                .entry(entry)
                .comment(entry.getComment())
                .build();
    }

}