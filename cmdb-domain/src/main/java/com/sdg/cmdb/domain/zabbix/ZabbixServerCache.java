package com.sdg.cmdb.domain.zabbix;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZabbixServerCache implements Serializable {
    private static final long serialVersionUID = -4349815352538017817L;

    public ZabbixServerCache() {
    }

    public ZabbixServerCache(long serverId, String hostid, String usedDiskSpaceItemid, String totalDiskSpaceItemid, String freeDiskSpaceItemid) {
        this.serverId = serverId;
        this.hostid = hostid;
        this.usedDiskSpaceItemid = usedDiskSpaceItemid;
        this.totalDiskSpaceItemid = totalDiskSpaceItemid;
        this.freeDiskSpaceItemid = freeDiskSpaceItemid;
    }

    private String hostid;
    private long serverId;

    /**
     * 总容量item
     */
    private String totalDiskSpaceItemid;
    /**
     * 空闲率item
     */
    private String freeDiskSpaceItemid;
    /**
     * 使用量item
     */
    private String usedDiskSpaceItemid;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
