package com.sdg.cmdb.domain.keybox;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class KeyboxLoginStatusDO implements Serializable {
    private static final long serialVersionUID = -2288311882090882506L;

    private long id;

    private long userId;

    //登陆类型  0 web  1 终端
    private int loginType = 0;

    private String username;

    private long serverId;

    private String gmtCreate;

    private String gmtModify;


    public KeyboxLoginStatusDO() {

    }

    public KeyboxLoginStatusDO(ServerDO serverDO, String username) {
        this.serverId = serverDO.getId();
        this.username = username;
    }

    public KeyboxLoginStatusDO(ServerDO serverDO, String username, int loginType) {
        this.serverId = serverDO.getId();
        this.username = username;
        this.loginType = loginType;
    }

    public KeyboxLoginStatusDO(ServerDO serverDO, UserDO userDO) {
        this.serverId = serverDO.getId();
        this.username = userDO.getUsername();
        this.userId = userDO.getId();
    }

    public enum LoginTypeEnum {
        //0 保留／在组中代表的是所有权限
        keybox(0, "keybox"),
        getway(1, "getway");
        private int code;
        private String desc;

        LoginTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getLoginTypeName(int code) {
            for (LoginTypeEnum loginTypeEnum : LoginTypeEnum.values()) {
                if (loginTypeEnum.getCode() == code) {
                    return loginTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    @Override
    public String toString() {
        return "KeyboxStatusDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", loginType=" + loginType +
                ", serverId=" + serverId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
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
