package com.sdg.cmdb.domain.keybox;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class KeyboxLoginStatusVO implements Serializable {
    private static final long serialVersionUID = -2727619496712192868L;


    //登陆类型  0 web  1 终端
    private int loginType = 0;

    private UserDO userDO;

    private String gmtCreate;

    private String gmtModify;

    private ServerDO serverDO;

    //历时显示
    private String timeView;

    public KeyboxLoginStatusVO() {

    }

    public KeyboxLoginStatusVO(KeyboxLoginStatusDO keyboxLoginStatusDO, UserDO userDO, ServerDO serverDO,String timeView) {
        this.loginType = keyboxLoginStatusDO.getLoginType();
        this.userDO = userDO;
        this.serverDO = serverDO;

        this.gmtCreate = keyboxLoginStatusDO.getGmtCreate();
        this.timeView=timeView;
    }

    @Override
    public String toString() {
        return "KeyboxStatusVO{" +
                ", loginType=" + loginType +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
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

    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }
}
