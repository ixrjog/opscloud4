package com.sdg.cmdb.domain.auth;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liangjian on 2017/6/29.
 */
public class UserLeaveVO extends UserLeaveDO implements Serializable {
    private static final long serialVersionUID = 8755384024624689966L;

    private String mailAccountStatus;
    /*
     0：未绑定；1：已绑定
    */
    private int ldap = 0;

    /**
     * 0：非zabbix用户  1：zabbix用户
     */
    private int zabbix = 0;

    private List<String> ldapGroups;

    public UserLeaveVO() {

    }

    public UserLeaveVO(UserLeaveDO userLeaveDO) {
        setId(userLeaveDO.getId());
        setUsername(userLeaveDO.getUsername());
        setDisplayName(userLeaveDO.getDisplayName());
        setMobile(userLeaveDO.getMobile());
        setMail(userLeaveDO.getMail());
        setGmtCreate(userLeaveDO.getGmtCreate());
    }

    public String getMailAccountStatus() {
        return mailAccountStatus;
    }

    public void setMailAccountStatus(String mailAccountStatus) {
        this.mailAccountStatus = mailAccountStatus;
    }

    public int getLdap() {
        return ldap;
    }

    public void setLdap(int ldap) {
        this.ldap = ldap;
    }

    public int getZabbix() {
        return zabbix;
    }

    public void setZabbix(int zabbix) {
        this.zabbix = zabbix;
    }

    public List<String> getLdapGroups() {
        return ldapGroups;
    }

    public void setLdapGroups(List<String> ldapGroups) {
        this.ldapGroups = ldapGroups;
    }
}
