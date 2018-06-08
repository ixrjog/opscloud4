package com.sdg.cmdb.domain.todo;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class TodoVpnDetailDO implements Serializable {
    private static final long serialVersionUID = -2029150120223185511L;

    public static final String VPN_LDAP_GRPOUP_NAME = "sslvpn-users";

    private long id;

    private long todoDetailId;

    private String ldapGroup = VPN_LDAP_GRPOUP_NAME;

    private boolean need = false;

    /**
     * 处理状态 0 未处理，1 处理完成  2 ERR
     */
    private int processStatus = 0;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "TodoVpnDetailDO{" +
                "id=" + id +
                ", todoDetailId=" + todoDetailId +
                ", ldapGroup='" + ldapGroup + '\'' +
                ", need='" + need + '\'' +
                ", processStatus=" + processStatus +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public TodoVpnDetailDO() {

    }

    public TodoVpnDetailDO(long todoDetailId) {
        this.todoDetailId = todoDetailId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTodoDetailId() {
        return todoDetailId;
    }

    public void setTodoDetailId(long todoDetailId) {
        this.todoDetailId = todoDetailId;
    }

    public String getLdapGroup() {
        return ldapGroup;
    }

    public void setLdapGroup(String ldapGroup) {
        this.ldapGroup = ldapGroup;
    }

    public boolean isNeed() {
        return need;
    }

    public void setNeed(boolean need) {
        this.need = need;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }
}
