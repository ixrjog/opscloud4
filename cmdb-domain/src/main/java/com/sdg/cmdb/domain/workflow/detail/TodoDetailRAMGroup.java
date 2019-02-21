package com.sdg.cmdb.domain.workflow.detail;

import java.io.Serializable;

public class TodoDetailRAMGroup extends TodoDetailAbs implements Serializable {

    private static final long serialVersionUID = 406770801104969000L;

    public TodoDetailRAMGroup() {

    }

    public TodoDetailRAMGroup(String groupName, String content) {
        this.groupName = groupName;
        setContent(content);
    }

    private String groupName;


    /**
     * 0 无权限， 1 有权限
     */
    private String groupValue = "0";

    /**
     * 申请RAM权限
     */
    private boolean apply = false;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }


}
