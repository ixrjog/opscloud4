package com.baiyi.opscloud.workorder.processor.impl.extended;

import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;

import java.util.Set;

/**
 * 用户组权限申请抽象类
 *
 * @Author baiyi
 * @Date 2022/1/7 10:56 AM
 * @Version 1.0
 */
public abstract class AbstractUserGroupPermissionExtendedAbstractUserPermission extends AbstractUserPermissionExtendedBaseTicketProcessor<UserGroup> {

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, UserGroup entry) throws TicketProcessException {
        checkName(entry);
        super.process(ticketEntry, entry);
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) {
        UserGroup entry = this.toEntry(ticketEntry.getContent());
        checkName(entry);
    }

    private void checkName(UserGroup entry) {
        if (getGroupNames().stream().noneMatch(groupName -> groupName.equals(entry.getName()))) {
            throw new TicketProcessException("授权条目不合规！");
        }
    }

    /**
     * 返回用户组名称
     *
     * @return
     */
    abstract public Set<String> getGroupNames();

    @Override
    protected Class<UserGroup> getEntryClassT() {
        return UserGroup.class;
    }

}