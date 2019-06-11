package com.sdg.cmdb.domain.workflow.detail;

import java.io.Serializable;

public class TodoDetailCIAndroidAuth extends TodoDetailAbs implements Serializable {
    private static final long serialVersionUID = -793447858532315072L;

    private long appId;       // 应用
    private String appName;   // 应用
    private String projectName;  // 应用-仓库


    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
