package com.sdg.cmdb.domain.ci.ciStatus;

import java.io.Serializable;

public class CiProjectVO implements Serializable {
    private static final long serialVersionUID = 8514805990854349842L;

    private String projectName;

    private int cnt;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
