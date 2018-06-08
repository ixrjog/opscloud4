package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

public class TodoBuilderPlanDetailDO implements Serializable {
    private static final long serialVersionUID = 6859095960006044801L;

    private long id;

    private long todoDetailId;

    private int envType;

    private int buildTool;

    private String branch;

    private boolean needEnvParams = true;

    private String envParams;

    /**
     * 处理状态 0 未处理，1 处理完成  2 ERR
     */
    private int processStatus =0;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    public enum BuildToolTypeEnum {
        //0 保留／在组中代表的是所有权限
        maven(0, "Maven"),
        gradle(1, "Gradle");

        private int code;
        private String desc;

        BuildToolTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getBuildToolTypeName(int code) {
            for (BuildToolTypeEnum buildToolTypeEnum : BuildToolTypeEnum.values()) {
                if (buildToolTypeEnum.getCode() == code) {
                    return buildToolTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }


    public TodoBuilderPlanDetailDO() {
    }

    @Override
    public String toString() {
        return "TodoBuilerPlanDetailDO{" +
                "id=" + id +
                ", todoDetailId=" + todoDetailId +
                ", envType=" + envType +
                ", buildTool=" + buildTool +
                ", needEnvParams=" + needEnvParams +
                ", branch='" + branch + '\'' +
                ", envParams='" + envParams + '\'' +
                ", content='" + content + '\'' +
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

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public String getEnvParams() {
        return envParams;
    }

    public void setEnvParams(String envParams) {
        this.envParams = envParams;
    }

    public int getBuildTool() {
        return buildTool;
    }

    public void setBuildTool(int buildTool) {
        this.buildTool = buildTool;
    }

    public boolean isNeedEnvParams() {
        return needEnvParams;
    }

    public void setNeedEnvParams(boolean needEnvParams) {
        this.needEnvParams = needEnvParams;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
