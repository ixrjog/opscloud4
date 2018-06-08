package com.sdg.cmdb.domain.auth;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/20.
 */
public class UserLeaveDO implements Serializable {
    private static final long serialVersionUID = -1549998090795982856L;

    private long id;

    private long userId;

    private String username;

    private String mobile;

    private String mail;

    private String displayName;

    private int dumpLdap = 0;

    private int dumpMail = 0;

    private String gmtCreate;

    private String gmtModify;

    public UserLeaveDO() {

    }

    public UserLeaveDO(UserDO userDO) {
        this.username = userDO.getUsername();

        this.userId = userDO.getId();

        if (!StringUtils.isEmpty(userDO.getDisplayName()))
            this.displayName = userDO.getDisplayName();

        if (!StringUtils.isEmpty(userDO.getMobile()))
            this.mobile = userDO.getMobile();

        if (!StringUtils.isEmpty(userDO.getMail()))
            this.mail = userDO.getMail();
    }


    public enum DumpTypeEnum {
        //0 保留
        def(0, "default"),
        success(1, "success"),
        fail(2, "fail");

        private int code;
        private String desc;

        DumpTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDumpTypeName(int code) {
            for (DumpTypeEnum dumpTypeEnum : DumpTypeEnum.values()) {
                if (dumpTypeEnum.getCode() == code) {
                    return dumpTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    @Override
    public String toString() {
        return "UserLeaveDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mail='" + mail + '\'' +
                ", displayName='" + displayName + '\'' +
                ", dumpLdap=" + dumpLdap +
                ", dumpMail=" + dumpMail +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public int getDumpLdap() {
        return dumpLdap;
    }

    public void setDumpLdap(int dumpLdap) {
        this.dumpLdap = dumpLdap;
    }

    public int getDumpMail() {
        return dumpMail;
    }

    public void setDumpMail(int dumpMail) {
        this.dumpMail = dumpMail;
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
