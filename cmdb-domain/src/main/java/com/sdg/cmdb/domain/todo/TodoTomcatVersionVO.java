package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

public class TodoTomcatVersionVO implements Serializable {


    public TodoTomcatVersionVO() {

    }

    public TodoTomcatVersionVO(String tomcatInstallVersion, String jdkInstallVersion) {
        this.tomcatInstallVersion = tomcatInstallVersion;
        this.jdkInstallVersion = jdkInstallVersion;
    }


    private static final long serialVersionUID = -603626069020345162L;

    private String tomcatInstallVersion;
    private String jdkInstallVersion;

    public String getTomcatInstallVersion() {
        return tomcatInstallVersion;
    }

    public void setTomcatInstallVersion(String tomcatInstallVersion) {
        this.tomcatInstallVersion = tomcatInstallVersion;
    }

    public String getJdkInstallVersion() {
        return jdkInstallVersion;
    }

    public void setJdkInstallVersion(String jdkInstallVersion) {
        this.jdkInstallVersion = jdkInstallVersion;
    }
}
