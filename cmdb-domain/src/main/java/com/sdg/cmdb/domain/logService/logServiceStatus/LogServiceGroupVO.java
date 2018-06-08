package com.sdg.cmdb.domain.logService.logServiceStatus;

import java.io.Serializable;

public class LogServiceGroupVO implements Serializable {
    private static final long serialVersionUID = 3101713641361464612L;

    private String groupName;

    private int cnt;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
