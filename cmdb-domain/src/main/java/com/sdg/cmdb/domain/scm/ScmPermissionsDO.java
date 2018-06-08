package com.sdg.cmdb.domain.scm;

import com.sdg.cmdb.domain.todo.todoProperty.StashProjectDO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class ScmPermissionsDO implements Serializable {
    private static final long serialVersionUID = -3128890881084509201L;

    private long id;

    private long ciUserGroupId;

    private String groupName;

    private String scmProject;

    private String scmDescription;

    private String gmtCreate;

    private String gmtModify;

    public ScmPermissionsDO() {

    }

    public ScmPermissionsDO(StashProjectDO stashProjectDO) {
        this.scmProject = stashProjectDO.getName();
        if (!StringUtils.isEmpty(stashProjectDO.getDescription()))
            this.scmDescription = stashProjectDO.getDescription();
    }

    @Override
    public String toString() {
        return "ScmPermissionsDO{" +
                "id=" + id +
                ", ciUserGroupId=" + ciUserGroupId +
                ", groupName='" + groupName + '\'' +
                ", scmProject=" + scmProject +
                ", scmDescription='" + scmDescription + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCiUserGroupId() {
        return ciUserGroupId;
    }

    public void setCiUserGroupId(long ciUserGroupId) {
        this.ciUserGroupId = ciUserGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getScmProject() {
        return scmProject;
    }

    public void setScmProject(String scmProject) {
        this.scmProject = scmProject;
    }

    public String getScmDescription() {
        return scmDescription;
    }

    public void setScmDescription(String scmDescription) {
        this.scmDescription = scmDescription;
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
