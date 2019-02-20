package com.sdg.cmdb.domain.ldap;

import java.io.Serializable;

public class LdapGroupDO implements Serializable {
    private static final long serialVersionUID = -1696953213720104790L;

    private long id;

    private String cn;

    private String content;

    /**
     * 是否允许工作流申请
     */
    private boolean workflow = true;

    private int groupType;

    private String gmtCreate;

    private String gmtModify;

    public LdapGroupDO() {

    }

    public LdapGroupDO(String cn, String content, int groupType) {
        this.cn = cn;
        this.content = content;
        this.groupType = groupType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isWorkflow() {
        return workflow;
    }

    public void setWorkflow(boolean workflow) {
        this.workflow = workflow;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
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
