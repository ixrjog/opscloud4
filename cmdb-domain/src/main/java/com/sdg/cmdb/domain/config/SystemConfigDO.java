package com.sdg.cmdb.domain.config;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/12/30.
 */
public class SystemConfigDO implements Serializable {
    private static final long serialVersionUID = -8943673060922155163L;

    /**
     * token有效期
     */
    private long token;

    private String gmtCreate;

    private String gmtModify;

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
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

    @Override
    public String toString() {
        return "SystemConfigDO{" +
                "token=" + token +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
