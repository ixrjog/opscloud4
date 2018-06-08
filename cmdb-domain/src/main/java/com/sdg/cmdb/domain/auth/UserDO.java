package com.sdg.cmdb.domain.auth;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/9/19.
 */
public class UserDO implements Serializable {
    private static final long serialVersionUID = 3444096336277588871L;

    private long id;

    private String mail;

    private String displayName;

    private String username;

    private String token;

    /*
    联系电话
     */
    private String mobile;

    /*
    登录密码
     */
    private String pwd;

    /*
    公钥
     */
    private String rsaKey;

    /*
    0：未授权；1：已授权
     */
    private int authed = 0;

    /*
    0：未授权；1：已授权
    */
    private int zabbixAuthed;

    /*
    是否无效。0：无效；1：有效
     */
    private int invalid;

    private List<ResourceDO> resourceDOList;

    private long roleId;

    /*
    机器数
     */
    private long servers;

    private String gmtCreate;

    private String gmtModify;

    public UserDO() {
    }

    public UserDO(String username) {
        this.username = username;
    }

    public UserDO(String mail, String displayName, String username, String token) {
        this.mail = mail;
        this.displayName = displayName;
        this.username = username;
        this.token = token;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }

    public int getAuthed() {
        return authed;
    }

    public void setAuthed(int authed) {
        this.authed = authed;
    }

    public int getZabbixAuthed() {
        return zabbixAuthed;
    }

    public void setZabbixAuthed(int zabbixAuthed) {
        this.zabbixAuthed = zabbixAuthed;
    }

    public int getInvalid() {
        return invalid;
    }

    public void setInvalid(int invalid) {
        this.invalid = invalid;
    }

    public List<ResourceDO> getResourceDOList() {
        return resourceDOList;
    }

    public void setResourceDOList(List<ResourceDO> resourceDOList) {
        this.resourceDOList = resourceDOList;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
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

    public long getServers() {
        return servers;
    }

    public void setServers(long servers) {
        this.servers = servers;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "mail='" + mail + '\'' +
                ", displayName='" + displayName + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", mobile='" + mobile + '\'' +
                ", pwd='" + pwd + '\'' +
                ", rsaKey='" + rsaKey + '\'' +
                ", authed=" + authed +
                ", zabbixAuthed=" + zabbixAuthed +
                ", invalid=" + invalid +
                ", resourceDOList=" + resourceDOList +
                ", roleId=" + roleId +
                ", servers=" + servers +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public enum ZabbixAuthType {
        noAuth(0, "未授权"),
        authed(1, "已授权");
        private int code;
        private String msg;

        ZabbixAuthType(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }


    public enum AuthType {
        noAuth(0, "未授权"),
        authed(1, "已授权");
        private int code;
        private String msg;

        AuthType(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum Invalid {
        invalid(0, "无效"),
        noInvalid(1, "有效");
        private int code;
        private String msg;

        Invalid(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
