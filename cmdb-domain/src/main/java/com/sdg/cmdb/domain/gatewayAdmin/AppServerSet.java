package com.sdg.cmdb.domain.gatewayAdmin;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AppServerSet implements Serializable {

    private static final long serialVersionUID = -6299220972613385720L;
    private String appName;
    private String healthCheckPath;
    private List<ServerConfigItem> serverConfigItems;

    public AppServerSet() {
    }

    public AppServerSet(String appName, String healthCheckPath, List<ServerConfigItem> serverConfigItems) {
        this.appName = appName;
        this.healthCheckPath = healthCheckPath;
        this.serverConfigItems = serverConfigItems;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
