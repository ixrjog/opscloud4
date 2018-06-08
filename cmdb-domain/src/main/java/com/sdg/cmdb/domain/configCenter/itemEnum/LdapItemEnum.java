package com.sdg.cmdb.domain.configCenter.itemEnum;

/**
 * Created by liangjian on 2017/5/31.
 */
public enum LdapItemEnum {
    LDAP_URL("LDAP_URL", "ldap://ldap.demo.com:10389"),
    LDAP_BIND_DN("LDAP_BIND_DN", "uid=admin,ou=system"),
    LDAP_PASSWD("LDAP_PASSWD", "管理员密码"),
    LDAP_USER_DN("LDAP_USER_DN", "ou=users,ou=system"),
    LDAP_GROUP_DN("LDAP_GROUP_DN", "ou=groups,ou=system"),
    LDAP_GROUP_FILTER("LDAP_GROUP_FILTER", "bamboo用户组前缀bamboo-"),
    LDAP_GROUP_PREFIX("LDAP_GROUP_PREFIX", "服务器组前缀group_"),
    LDAP_GROUP_JIRA_USERS("LDAP_GROUP_JIRA_USERS", "jira普通用户组"),
    LDAP_GROUP_CONFLUENCE_USERS("LDAP_GROUP_CONFLUENCE_USERS", "confluence普通用户组"),
    LDAP_MAIL_URL("LDAP_MAIL_URL", "邮件服务器LDAP:ldap://ldap.demo.com:10389"),
    LDAP_MAIL_BIND_DN("LDAP_MAIL_BIND_DN", "邮件服务器LDAP:uid=zimbra,cn=admins,cn=zimbra"),
    LDAP_MAIL_PASSWD("LDAP_MAIL_PASSWD", "邮件服务器LDAP管理员密码"),
    LDAP_MAIL_USER_DN("LDAP_MAIL_USER_DN", "邮件服务器LDAP:ou=people,dc=51xianqu,dc=net"),;

    private String itemKey;
    private String itemDesc;

    LdapItemEnum(String itemKey, String itemDesc) {
        this.itemKey = itemKey;
        this.itemDesc = itemDesc;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemDescByKey(String itemKey) {
        for (LdapItemEnum itemEnum : LdapItemEnum.values()) {
            if (itemEnum.getItemKey().equals(itemKey)) {
                return itemEnum.getItemDesc();
            }
        }
        return null;
    }
}