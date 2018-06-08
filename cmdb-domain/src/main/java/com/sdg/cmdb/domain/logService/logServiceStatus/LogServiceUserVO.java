package com.sdg.cmdb.domain.logService.logServiceStatus;

import java.io.Serializable;

public class LogServiceUserVO implements Serializable {
    private static final long serialVersionUID = 2717133711684276418L;


    private long userId;

    private String username;

    private int cnt;


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

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
