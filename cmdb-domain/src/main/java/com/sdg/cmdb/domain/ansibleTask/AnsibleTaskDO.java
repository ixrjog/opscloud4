package com.sdg.cmdb.domain.ansibleTask;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class AnsibleTaskDO implements Serializable {

    private static final long serialVersionUID = -6006619876706929043L;

    public static  final int TASK_TYPE_CMD = 0;

    public static  final int TASK_TYPE_SCRIPT = 1;

    public static  final int TASK_TYPE_COPY = 2;

    private long id;

    private long userId;

    private String userName;

    private int serverCnt;

    // 0:cmd/1:script
    private int taskType = 0;

    private String cmd;

    private long taskScriptId;

    private boolean finalized = false;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "AnsibleTaskDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", serverCnt=" + serverCnt +
                ", taskType=" + taskType  +
                ", cmd='" + cmd + '\'' +
                ", taskScriptId=" + taskScriptId +
                ", finalized='" + finalized + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public AnsibleTaskDO(UserDO userDO, int serverCnt, String cmd) {
        this.userId = userDO.getId();
        this.userName = userDO.getUsername();
        this.serverCnt = serverCnt;
        this.cmd = cmd;
    }

    public AnsibleTaskDO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getServerCnt() {
        return serverCnt;
    }

    public void setServerCnt(int serverCnt) {
        this.serverCnt = serverCnt;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public long getTaskScriptId() {
        return taskScriptId;
    }

    public void setTaskScriptId(long taskScriptId) {
        this.taskScriptId = taskScriptId;
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
