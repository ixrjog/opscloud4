package com.sdg.cmdb.domain.gatewayAdmin;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppSetVO implements Serializable {

    private String appSet;
    private String appServerSet;
    private GatewayAdminDO gatewayAdminDO;

    public AppSetVO() {

    }

    public AppSetVO(GatewayAdminDO gatewayAdminDO, String appSet, String appServerSet) {
        this.gatewayAdminDO = gatewayAdminDO;
        this.appSet = appSet;
        this.appServerSet = appServerSet;
    }

}
