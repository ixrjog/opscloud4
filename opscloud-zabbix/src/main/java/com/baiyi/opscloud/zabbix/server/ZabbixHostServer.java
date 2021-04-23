package com.baiyi.opscloud.zabbix.server;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.entry.ZabbixMacro;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.param.ZabbixDefaultParam;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:10 下午
 * @Version 1.0
 */
public interface ZabbixHostServer {

    void evictHost(String mgmtIp);

    ZabbixHost getHost(String mgmtIp);

    List<ZabbixTemplate> getHostTemplates(String hostid);

    List<ZabbixMacro> getHostMacros(String hostid);

    ZabbixHost getHostByHostid(String hostid);

    List<ZabbixHost> getHostList();

    List<ZabbixHostInterface> getHostInterfaces(String hostid);

    BusinessWrapper<Boolean> updateHostStatus(String hostid, int status);

    BusinessWrapper<Boolean> massUpdateHostStatus(List<String> hostids, int status);

    BusinessWrapper<Boolean> updateHostTemplates(String hostid, Map<String, String> templateMap);

    boolean updateHostTags(String hostid, List<ZabbixDefaultParam> tags);

    boolean clearHostTemplates(String hostid, Map<String, String> clearTemplateMap);

    boolean updateHostMacros(String hostid, String macros);


}
