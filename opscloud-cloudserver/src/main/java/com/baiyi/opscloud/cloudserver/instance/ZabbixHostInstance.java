package com.baiyi.opscloud.cloudserver.instance;

import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2019/11/29 2:37 PM
 * @Version 1.0
 */
@Data
public class ZabbixHostInstance implements Serializable {
    private static final long serialVersionUID = -4751526482819186851L;

    private ZabbixHost host;
    private List<ZabbixHostInterface> interfaceList;

    public ZabbixHostInstance() {
    }

    public ZabbixHostInstance(ZabbixHost host, List<ZabbixHostInterface> interfaceList) {
        this.host = host;
        this.interfaceList = interfaceList;
    }

}
