package com.baiyi.opscloud.zabbix.server;

import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:10 下午
 * @Version 1.0
 */
public interface ZabbixHostServer {

    // host
    ZabbixHost getHost(String mgmtIp);

    List<ZabbixTemplate> getHostTemplates(String hostid);

    ZabbixHost getHostByHostid(String hostid);

    List<ZabbixHost> getHostList();

    List<ZabbixHostInterface> getHostInterfaceList(String hostid);

    boolean updateHostStatus(String hostid, int status);

    boolean updateHostTemplates(String hostid, Map<String, String> templateMap);

    boolean clearHostTemplates(String hostid, Map<String, String> clearTemplateMap);

    boolean updateHostMacros(String hostid, HashMap<String, String> macrosMap);

}
