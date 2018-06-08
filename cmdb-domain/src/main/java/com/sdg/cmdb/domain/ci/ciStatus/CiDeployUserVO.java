package com.sdg.cmdb.domain.ci.ciStatus;

import java.io.Serializable;

public class CiDeployUserVO implements Serializable {
    private static final long serialVersionUID = 5712202224938300415L;

    private String username;

    private int cnt;

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
