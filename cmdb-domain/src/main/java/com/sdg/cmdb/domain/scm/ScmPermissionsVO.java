package com.sdg.cmdb.domain.scm;

import com.sdg.cmdb.domain.auth.CiUserGroupDO;

import java.io.Serializable;

public class ScmPermissionsVO extends ScmPermissionsDO implements Serializable {
    private static final long serialVersionUID = 5769934657458958297L;

    public ScmPermissionsVO() {

    }

    public ScmPermissionsVO(ScmPermissionsDO scmPermissionsDO, CiUserGroupDO ciUserGroupDO) {
        setId(scmPermissionsDO.getId());
        setCiUserGroupId(scmPermissionsDO.getCiUserGroupId());
        setGroupName(scmPermissionsDO.getGroupName());
        setScmProject(scmPermissionsDO.getScmProject());
        setScmDescription(scmPermissionsDO.getScmDescription());
        setGmtCreate(scmPermissionsDO.getGmtCreate());
        setGmtModify(scmPermissionsDO.getGmtModify());
        if (ciUserGroupDO != null)
            this.ciUserGroupDO = ciUserGroupDO;
    }

    public ScmPermissionsVO(ScmPermissionsDO scmPermissionsDO) {
        setId(scmPermissionsDO.getId());
        setCiUserGroupId(scmPermissionsDO.getCiUserGroupId());
        setGroupName(scmPermissionsDO.getGroupName());
        setScmProject(scmPermissionsDO.getScmProject());
        setScmDescription(scmPermissionsDO.getScmDescription());
        setGmtCreate(scmPermissionsDO.getGmtCreate());
        setGmtModify(scmPermissionsDO.getGmtModify());
    }

    private CiUserGroupDO ciUserGroupDO;

    public CiUserGroupDO getCiUserGroupDO() {
        return ciUserGroupDO;
    }

    public void setCiUserGroupDO(CiUserGroupDO ciUserGroupDO) {
        this.ciUserGroupDO = ciUserGroupDO;
    }
}
