package com.sdg.cmdb.domain.workflow.detail;


import com.sdg.cmdb.domain.auth.CiUserGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

public class TodoDetailKeybox extends TodoDetailAbs implements Serializable{

    private static final long serialVersionUID = 1964828516800144159L;

    private ServerGroupDO serverGroupDO;

    private CiUserGroupDO ciUserGroupDO;

    private boolean ciAuth = false;

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }

    public CiUserGroupDO getCiUserGroupDO() {
        return ciUserGroupDO;
    }

    public void setCiUserGroupDO(CiUserGroupDO ciUserGroupDO) {
        this.ciUserGroupDO = ciUserGroupDO;
    }

    public boolean isCiAuth() {
        return ciAuth;
    }

    public void setCiAuth(boolean ciAuth) {
        this.ciAuth = ciAuth;
    }
}
