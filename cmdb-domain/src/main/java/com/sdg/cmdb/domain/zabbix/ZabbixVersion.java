package com.sdg.cmdb.domain.zabbix;

import java.io.Serializable;

/**
 * 用于校验ZabbixAPI配置是否生效
 */
public class ZabbixVersion implements Serializable {

    private String version;

    // "name": "Zabbix server"
    private String name;

    //  "hostid": "10160",
    private String hostid;



    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostid() {
        return hostid;
    }

    public void setHostid(String hostid) {
        this.hostid = hostid;
    }

}
