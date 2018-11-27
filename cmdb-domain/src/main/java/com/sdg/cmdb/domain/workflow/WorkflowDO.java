package com.sdg.cmdb.domain.workflow;

import java.io.Serializable;

public class WorkflowDO implements Serializable {
    private static final long serialVersionUID = 1430354332190209812L;

    private long id;

    private long groupId;

    private String wfName;

    private String wfKey;

    /**
     * 工单状态 0正常 1暂时关闭 2开发中
     */
    private int wfStatus;

    private String title;

    private String content;

    private String helpUrl;

    /**
     * 主题（用于搜索）
     */
    private String topics;

    /**
     * 工单类型
     */
    private int wfType;

    public enum WfTypeEnum {
        operation(0, "operation");
        private int code;
        private String desc;

        WfTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getWfTypeName(int code) {
            for (WfTypeEnum wfTypeEnum : WfTypeEnum.values()) {
                if (wfTypeEnum.getCode() == code) {
                    return wfTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    /**
     * 是否 审批
     */
    private boolean approval;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getWfName() {
        return wfName;
    }

    public void setWfName(String wfName) {
        this.wfName = wfName;
    }

    public String getWfKey() {
        return wfKey;
    }

    public void setWfKey(String wfKey) {
        this.wfKey = wfKey;
    }

    public int getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(int wfStatus) {
        this.wfStatus = wfStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public int getWfType() {
        return wfType;
    }

    public void setWfType(int wfType) {
        this.wfType = wfType;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
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
