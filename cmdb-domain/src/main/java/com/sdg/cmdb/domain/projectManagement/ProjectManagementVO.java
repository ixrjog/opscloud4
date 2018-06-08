package com.sdg.cmdb.domain.projectManagement;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;
import java.util.List;

public class ProjectManagementVO implements Serializable {
    private static final long serialVersionUID = -270632519477611600L;

    private long id;

    private String projectName;

    private String content;

    private String beginTime;

    private int projectType;

    private long leaderUserId;

    private String leaderUsername;

    private UserDO leaderUser;

    private int ttl;

    // 心跳时间差
    private int dateDiff = -1;

    private boolean needHeartbeat = false;

    private List<ProjectPropertyVO> users;

    private List<ProjectPropertyVO> serverGroups;

    private List<ProjectHeartbeatVO> heartbeats;

    private String timeView;

    private int status;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "ProjectManagementVO{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", content='" + content + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", projectType=" + projectType +
                ", ttl=" + ttl +
                ", leaderUserId=" + leaderUserId +
                ", leaderUsername='" + leaderUsername + '\'' +
                ", status=" + status +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public ProjectManagementVO() {

    }

    public ProjectManagementVO(ProjectManagementDO projectManagementDO) {
        this.id = projectManagementDO.getId();
        this.projectName = projectManagementDO.getProjectName();
        this.content = projectManagementDO.getContent();
        this.leaderUserId = projectManagementDO.getLeaderUserId();
        this.leaderUsername = projectManagementDO.getLeaderUsername();
        this.beginTime = projectManagementDO.getBeginTime();
        this.projectType = projectManagementDO.getProjectType();
        this.ttl = projectManagementDO.getTtl();
        this.status = projectManagementDO.getStatus();
        this.gmtCreate = projectManagementDO.getGmtCreate();
        this.gmtModify = projectManagementDO.getGmtModify();
    }

    public ProjectManagementVO(ProjectManagementDO projectManagementDO,int status) {
        this(projectManagementDO);
        this.status = status;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getProjectType() {
        return projectType;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    public long getLeaderUserId() {
        return leaderUserId;
    }

    public void setLeaderUserId(long leaderUserId) {
        this.leaderUserId = leaderUserId;
    }

    public String getLeaderUsername() {
        return leaderUsername;
    }

    public void setLeaderUsername(String leaderUsername) {
        this.leaderUsername = leaderUsername;
    }

    public List<ProjectPropertyVO> getUsers() {
        return users;
    }

    public void setUsers(List<ProjectPropertyVO> users) {
        this.users = users;
    }

    public List<ProjectPropertyVO> getServerGroups() {
        return serverGroups;
    }

    public void setServerGroups(List<ProjectPropertyVO> serverGroups) {
        this.serverGroups = serverGroups;
    }

    public List<ProjectHeartbeatVO> getHeartbeats() {
        return heartbeats;
    }

    public void setHeartbeats(List<ProjectHeartbeatVO> heartbeats) {
        this.heartbeats = heartbeats;
    }

    public UserDO getLeaderUser() {
        return leaderUser;
    }

    public void setLeaderUser(UserDO leaderUser) {
        this.leaderUser = leaderUser;
    }

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDateDiff() {
        return dateDiff;
    }

    public void setDateDiff(int dateDiff) {
        this.dateDiff = dateDiff;
    }

    public boolean isNeedHeartbeat() {
        return needHeartbeat;
    }

    public void setNeedHeartbeat(boolean needHeartbeat) {
        this.needHeartbeat = needHeartbeat;
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
}
