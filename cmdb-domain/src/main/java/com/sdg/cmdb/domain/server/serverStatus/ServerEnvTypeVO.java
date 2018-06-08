package com.sdg.cmdb.domain.server.serverStatus;

import com.sdg.cmdb.domain.server.ServerDO;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class ServerEnvTypeVO implements Serializable {
    private static final long serialVersionUID = 1682225101669678916L;

    // envTypeName
    private String name;

    private int envType;

    private int cnt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
        this.name = ServerDO.EnvTypeEnum.getEnvTypeName(envType);
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
