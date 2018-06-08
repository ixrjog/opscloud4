package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/8/9.
 */
public class LdapGroupVO implements Serializable {
    private static final long serialVersionUID = 1666739342255019488L;

    private String name;

    private int type;


    public LdapGroupVO() {

    }

    public LdapGroupVO(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public enum LdapGroupTypeEnum {
        //0 保留
        bamboo(0, "bamboo-"),
        confluence_users(1, "confluence-users"),
        confluence_administrators(2, "confluence-administrators"),
        confluence_admin(3, "confluence-admin"),
        jira_users(4, "jira-users"),
        jira_administrators(5, "jira-administrators"),
        stash_admin(6, "stash-admin"),
        crowd_admin(7, "crowd-admin"),
        administrators(8, "Administrators"),
        nexus_developer(9, "nexus-developer"),
        nexus_administrators(10, "nexus-administrators")
        ;
        private int code;
        private String desc;

        LdapGroupTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getEnvTypeName(int code) {
            for (LdapGroupTypeEnum ldapGroupTypeEnum : LdapGroupTypeEnum.values()) {
                if (ldapGroupTypeEnum.getCode() == code) {
                    return ldapGroupTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


}
