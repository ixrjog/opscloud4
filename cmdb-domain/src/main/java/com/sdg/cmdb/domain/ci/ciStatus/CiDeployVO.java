package com.sdg.cmdb.domain.ci.ciStatus;

import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;
import java.util.List;

public class CiDeployVO implements Serializable {
    private static final long serialVersionUID = 5627114368423997220L;

    private String projectName;

    private String groupName;

    private String version;

    private String username;

    private List<ServerDO> servers;

    private String gmtCreate;

    private String gmtModify;

    private String timeView;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ServerDO> getServers() {
        return servers;
    }

    public void setServers(List<ServerDO> servers) {
        this.servers = servers;
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

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }
}
