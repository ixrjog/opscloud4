package com.sdg.cmdb.domain.workflow.detail;

import com.sdg.cmdb.domain.ldap.LdapGroupDO;

import java.io.Serializable;


public class TodoDetailLdapGroup extends TodoDetailAbs implements Serializable {
    private static final long serialVersionUID = 2175445562971024515L;

    /**
     * 申请
     */
    private boolean apply = false;
    /**
     * 是否绑定
     */
    private boolean bind = false;
    private LdapGroupDO ldapGroupDO;

    public TodoDetailLdapGroup() {

    }

    public TodoDetailLdapGroup(LdapGroupDO ldapGroupDO,boolean bind) {
        this.ldapGroupDO = ldapGroupDO;
        this.bind = bind;
    }

    public TodoDetailLdapGroup(LdapGroupDO ldapGroupDO,  boolean bind,boolean apply) {
        this.ldapGroupDO = ldapGroupDO;
        this.apply = apply;
        this.bind = bind;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public LdapGroupDO getLdapGroupDO() {
        return ldapGroupDO;
    }

    public void setLdapGroupDO(LdapGroupDO ldapGroupDO) {
        this.ldapGroupDO = ldapGroupDO;
    }
}
