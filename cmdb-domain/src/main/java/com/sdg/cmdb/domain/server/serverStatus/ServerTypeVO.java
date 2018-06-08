package com.sdg.cmdb.domain.server.serverStatus;

import com.sdg.cmdb.domain.server.ServerDO;

import java.io.Serializable;

public class ServerTypeVO implements Serializable {

    private static final long serialVersionUID = 2409645178449599280L;

    private String name;

    private int serverType;

    private int cnt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
        this.name = ServerDO.ServerTypeEnum.getServerTypeName(serverType);
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
