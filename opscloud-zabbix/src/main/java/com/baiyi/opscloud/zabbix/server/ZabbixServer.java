package com.baiyi.opscloud.zabbix.server;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.zabbix.entry.ZabbixAction;
import com.baiyi.opscloud.zabbix.entry.ZabbixProxy;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @Author baiyi
 * @Date 2019/12/31 5:56 下午
 * @Version 1.0
 */
public interface ZabbixServer {

    ZabbixTemplate getTemplate(String name);

    ZabbixProxy getProxy(String hostname);

    void deleteAction(String usergrpName);

    ZabbixAction getAction(String usergrpName);

    boolean createAction(String usergroupName);

    BusinessWrapper<Boolean> result(JsonNode jsonNode);
}
