package com.sdg.cmdb.domain.copy;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

public class CopyServerVO extends CopyServerDO implements Serializable {

    private static final long serialVersionUID = 4386673146752252322L;
    private ServerDO serverDO;

    private ServerGroupDO serverGroupDO;

    public CopyServerVO() {

    }

    public CopyServerVO(CopyServerDO copyServerDO) {
        setId(copyServerDO.getId());
        setCopyId(copyServerDO.getCopyId());
        setServerId(copyServerDO.getServerId());
        setGmtCreate(copyServerDO.getGmtCreate());
        setGmtModify(copyServerDO.getGmtModify());
    }

    public ServerDO getServerDO() {
        return serverDO;
    }

    public void setServerDO(ServerDO serverDO) {
        this.serverDO = serverDO;
    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }
}
