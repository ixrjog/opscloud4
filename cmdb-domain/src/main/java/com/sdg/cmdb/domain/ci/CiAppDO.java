package com.sdg.cmdb.domain.ci;


import lombok.Data;

import java.io.Serializable;

@Data
public class CiAppDO implements Serializable {
    private static final long serialVersionUID = 4958963658703185528L;

    public CiAppDO() {

    }

    public CiAppDO(CiAppVO ciAppVO) {
        this.id = ciAppVO.getId();
        this.appName = ciAppVO.getAppName();
        this.projectName = ciAppVO.getProjectName();
        if (ciAppVO.getServerGroupDO() == null) {
            this.serverGroupId = ciAppVO.getServerGroupId();
            this.serverGroupName = ciAppVO.getServerGroupName();
        } else {
            this.serverGroupId = ciAppVO.getServerGroupDO().getId();
            this.serverGroupName = ciAppVO.getServerGroupDO().getName();
        }
        this.dingtalkId = ciAppVO.getDingtalkId();
        this.content = ciAppVO.getContent();
        this.sshUrl = ciAppVO.getSshUrl();
        this.webUrl = ciAppVO.getWebUrl();
        this.projectId = ciAppVO.getProjectId();
        this.ciType = ciAppVO.getCiType();
        this.appType = ciAppVO.getAppType();
        this.authBranch = ciAppVO.isAuthBranch();
        this.gmtCreate = ciAppVO.getGmtCreate();
        this.gmtModify = ciAppVO.getGmtModify();
        this.userId = ciAppVO.getUserId();
    }

    private long id;
    private String appName;
    private String projectName;

    private long serverGroupId;

    private String serverGroupName;

    private long dingtalkId;

    private String content;

    private String sshUrl;

    private String webUrl;

    private int projectId;

    private int ciType;

    private int appType;

    private long userId;

    private boolean authBranch;

    private String gmtCreate;

    private String gmtModify;



}
