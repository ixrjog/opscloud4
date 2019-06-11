package com.sdg.cmdb.domain.gatewayAdmin;

import com.sdg.cmdb.domain.server.ServerDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class ServerConfigItem implements Serializable {
    private static final long serialVersionUID = -4839402437831573283L;

    private String host;
    private String serverName;

    public ServerConfigItem() {
    }

    public ServerConfigItem(ServerDO serverDO, String port) {
        this.serverName = serverDO.acqServerName();
        this.host = serverDO.getInsideIp();
        if (!port.equals("80")) // 非标准HTTP端口
            this.host += ":" + port;
    }

    public ServerConfigItem(ServerDO serverDO, int nodePort) {
        this.serverName = serverDO.acqServerName();
        this.host = serverDO.getInsideIp() + ":" + nodePort;
    }

    public ServerConfigItem(String host, String serverName) {
        this.host = host;
        this.serverName = serverName;
    }

}
