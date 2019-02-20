package com.sdg.cmdb.domain.ci;

import java.io.Serializable;


public class CiDeployDO implements Serializable {
    private static final long serialVersionUID = -8109545457785508635L;

    private long id;
    private long serverId;
    private long ciHistoryId;
    private String gmtCreate;
    private String gmtModify;

    public CiDeployDO() {

    }

    public CiDeployDO(long serverId, long ciHistoryId) {
        this.serverId = serverId;
        this.ciHistoryId = ciHistoryId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public long getCiHistoryId() {
        return ciHistoryId;
    }

    public void setCiHistoryId(long ciHistoryId) {
        this.ciHistoryId = ciHistoryId;
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
