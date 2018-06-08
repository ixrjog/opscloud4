package com.sdg.cmdb.domain.jenkins.webhook;


import java.io.Serializable;

public class HookAndroidDebugNote implements Serializable {
    private static final long serialVersionUID = 5280522137972956341L;

    private String project;

    private String apk;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }
}
