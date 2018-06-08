package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

public class TodoVpnDetailVO extends TodoVpnDetailDO implements Serializable {
    private static final long serialVersionUID = -3863269666961272752L;

    /**
     * 是否授权（已开通VPN权限）
     */
    private boolean authed;


    public TodoVpnDetailVO() {

    }

    public TodoVpnDetailVO(TodoVpnDetailDO todoVpnDetailDO, boolean isAuthed) {
        this.authed = isAuthed;
        setId(todoVpnDetailDO.getId());
        setTodoDetailId(todoVpnDetailDO.getTodoDetailId());
        setLdapGroup(todoVpnDetailDO.getLdapGroup());
        setNeed(todoVpnDetailDO.isNeed());
        setProcessStatus(todoVpnDetailDO.getProcessStatus());
        setGmtCreate(todoVpnDetailDO.getGmtCreate());
        setGmtModify(todoVpnDetailDO.getGmtModify());
    }

    public boolean isAuthed() {
        return authed;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }
}
