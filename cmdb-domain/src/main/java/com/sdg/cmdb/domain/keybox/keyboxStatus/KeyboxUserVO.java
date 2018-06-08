package com.sdg.cmdb.domain.keybox.keyboxStatus;

import java.io.Serializable;

public class KeyboxUserVO implements Serializable {

    private static final long serialVersionUID = 6162421962453071308L;
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
