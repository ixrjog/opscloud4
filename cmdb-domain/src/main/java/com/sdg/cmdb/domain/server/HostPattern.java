package com.sdg.cmdb.domain.server;

import java.io.Serializable;
import java.util.List;

public class HostPattern implements Serializable {

    private static final long serialVersionUID = 5846047092689789990L;

    private String hostPattern;

    private List<ServerDO> servers;

    public HostPattern(){

    }

    public HostPattern(String hostPattern,List<ServerDO> servers){
        this.hostPattern =hostPattern;
        this.servers = servers;
    }

    public String getHostPattern() {
        return hostPattern;
    }

    public void setHostPattern(String hostPattern) {
        this.hostPattern = hostPattern;
    }

    public List<ServerDO> getServers() {
        return servers;
    }

    public void setServers(List<ServerDO> servers) {
        this.servers = servers;
    }
}
