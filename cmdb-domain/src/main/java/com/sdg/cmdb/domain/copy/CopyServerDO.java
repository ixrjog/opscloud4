package com.sdg.cmdb.domain.copy;

import java.io.Serializable;

public class CopyServerDO implements Serializable {
    private static final long serialVersionUID = 4286700360842519689L;

    private long id;

    private long copyId;

    private long serverId;

    private String gmtCreate;

    private String gmtModify;

    public CopyServerDO(long copyId, long serverId) {
        this.copyId = copyId;
        this.serverId = serverId;
    }

    public CopyServerDO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCopyId() {
        return copyId;
    }

    public void setCopyId(long copyId) {
        this.copyId = copyId;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
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
