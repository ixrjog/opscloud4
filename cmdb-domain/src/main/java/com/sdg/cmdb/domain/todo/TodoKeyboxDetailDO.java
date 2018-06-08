package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/9/6.
 */
public class TodoKeyboxDetailDO implements Serializable {
    private static final long serialVersionUID = -2822538712363871206L;

    private long id;

    private long todoDetailId;

    private long serverGroupId;

    private String serverGroupName;

    private boolean ciAuth;

    private long ciUserGroupId;

    private String ciUserGroupName;

    /**
     * 处理状态 0 未处理，1 处理完成  2 ERR
     */
    private int processStatus;

    private String gmtCreate;

    private String gmtModify;

    public enum ProcessStatusEnum {
        def(0, "默认"),
        complete(1, "处理完成"),
        err(2, "审批完成");

        private int code;
        private String desc;

        ProcessStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getProcessStatusName(int code) {
            for (ProcessStatusEnum processStatusEnum : ProcessStatusEnum.values()) {
                if (processStatusEnum.getCode() == code) {
                    return processStatusEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    @Override
    public String toString() {
        return "TodoKeyboxDetailDO{" +
                "id=" + id +
                ", todoDetailId=" + todoDetailId +
                ", serverGroupId=" + serverGroupId +
                ", serverGroupName='" + serverGroupName + '\'' +
                ", ciAuth=" + ciAuth +
                ", ciUserGroupId=" + ciUserGroupId +
                ", ciUserGroupName='" + ciUserGroupName + '\'' +
                ", processStatus=" + processStatus +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTodoDetailId() {
        return todoDetailId;
    }

    public void setTodoDetailId(long todoDetailId) {
        this.todoDetailId = todoDetailId;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public String getServerGroupName() {
        return serverGroupName;
    }

    public void setServerGroupName(String serverGroupName) {
        this.serverGroupName = serverGroupName;
    }

    public boolean isCiAuth() {
        return ciAuth;
    }

    public void setCiAuth(boolean ciAuth) {
        this.ciAuth = ciAuth;
    }

    public long getCiUserGroupId() {
        return ciUserGroupId;
    }

    public void setCiUserGroupId(long ciUserGroupId) {
        this.ciUserGroupId = ciUserGroupId;
    }

    public String getCiUserGroupName() {
        return ciUserGroupName;
    }

    public void setCiUserGroupName(String ciUserGroupName) {
        this.ciUserGroupName = ciUserGroupName;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
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
