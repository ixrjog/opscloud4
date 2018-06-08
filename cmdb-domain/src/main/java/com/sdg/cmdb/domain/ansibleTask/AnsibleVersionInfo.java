package com.sdg.cmdb.domain.ansibleTask;

import java.io.Serializable;

public class AnsibleVersionInfo implements Serializable {
    private static final long serialVersionUID = -7017041489863122167L;

    // 详细版本信息
    private String versionInfo;

    private String version;

    private String ansibleBinPath;

    private String ansibleTaskScriptPath;

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAnsibleBinPath() {
        return ansibleBinPath;
    }

    public void setAnsibleBinPath(String ansibleBinPath) {
        this.ansibleBinPath = ansibleBinPath;
    }

    public String getAnsibleTaskScriptPath() {
        return ansibleTaskScriptPath;
    }

    public void setAnsibleTaskScriptPath(String ansibleTaskScriptPath) {
        this.ansibleTaskScriptPath = ansibleTaskScriptPath;
    }
}
