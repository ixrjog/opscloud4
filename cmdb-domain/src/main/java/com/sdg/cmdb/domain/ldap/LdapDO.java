package com.sdg.cmdb.domain.ldap;

import com.sdg.cmdb.domain.configCenter.itemEnum.LdapItemEnum;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by zxxiao on 2017/6/23.
 */
public class LdapDO implements Serializable {
    private static final long serialVersionUID = -7142809943936080039L;

    private String url;
    private String base = "";
    private String userDn;
    private String password;
    private String ldapType;

    @Override
    public String toString() {
        return "LdapDO{" +
                "ldapType='" + ldapType + '\'' +
                ", url='" + url + '\'' +
                ", base='" + base + '\'' +
                ", userDn='" + userDn + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public LdapDO() {

    }

    public LdapDO(String url, String userDn, String password) {
        this.url = url;
        this.userDn = userDn;
        this.password = password;
    }

    public LdapDO(HashMap<String, String> configMap, String ldapType) {
        this.ldapType = ldapType;
        if (ldapType.equalsIgnoreCase(LdapTypeEnum.cmdb.getDesc())) {
            this.url = configMap.get(LdapItemEnum.LDAP_URL.getItemKey());
            this.userDn = configMap.get(LdapItemEnum.LDAP_BIND_DN.getItemKey());
            this.password = configMap.get(LdapItemEnum.LDAP_PASSWD.getItemKey());
        }
        if (ldapType.equalsIgnoreCase(LdapTypeEnum.mail.getDesc())) {
            this.url = configMap.get(LdapItemEnum.LDAP_MAIL_URL.getItemKey());
            this.userDn = configMap.get(LdapItemEnum.LDAP_MAIL_BIND_DN.getItemKey());
            this.password = configMap.get(LdapItemEnum.LDAP_MAIL_PASSWD.getItemKey());
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getUserDn() {
        return userDn;
    }

    public void setUserDn(String userDn) {
        this.userDn = userDn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLdapType() {
        return ldapType;
    }

    public void setLdapType(String ldapType) {
        this.ldapType = ldapType;
    }

    public enum LdapTypeEnum {
        //0 保留
        cmdb(0, "cmdb"),
        mail(1, "mail");

        private int code;
        private String desc;

        LdapTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getLdapTypeName(int code) {
            for (LdapTypeEnum ldapTypeEnum : LdapTypeEnum.values()) {
                if (ldapTypeEnum.getCode() == code) {
                    return ldapTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

}
