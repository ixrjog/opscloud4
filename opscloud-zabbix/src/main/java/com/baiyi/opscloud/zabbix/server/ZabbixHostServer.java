package com.baiyi.opscloud.zabbix.server;

import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:10 下午
 * @Version 1.0
 */
public interface ZabbixHostServer {

    // host
    ZabbixHost getHost(String mgmtIp);

    ZabbixHost getHostByHostid(String hostid);

    List<ZabbixHost> getHostList();

    List<ZabbixHostInterface> getHostInterfaceList(String hostid);

    ZabbixHost updateHostStatus(String hostid, int status);

}
