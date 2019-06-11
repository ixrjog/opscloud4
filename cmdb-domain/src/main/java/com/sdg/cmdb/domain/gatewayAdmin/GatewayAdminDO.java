package com.sdg.cmdb.domain.gatewayAdmin;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class GatewayAdminDO implements Serializable {
    private static final long serialVersionUID = 1285236894254103871L;
    private long id;
    private long serverGroupId;
    private String serverGroupName;
    private String appName;
    private String routePath;
    private String healthCheckPath;
    private String serviceTest;
    private String servicePre;
    private boolean prodSuccess = false;
    private boolean preSuccess = false;
    private boolean testSuccess = false;
    private String gmtCreate;
    private String gmtModify;

    public GatewayAdminDO() {
    }

    public GatewayAdminDO(String appName, ServerGroupDO serverGroupDO) {
        this.id = 0;
        this.appName = appName;
        this.serverGroupId = serverGroupDO.getId();
        this.serverGroupName = serverGroupDO.getName();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
