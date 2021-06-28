package com.baiyi.caesar.zabbix.http;

import com.baiyi.caesar.zabbix.entry.ZabbixUser;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 4:17 下午
 * @Since 1.0
 */
public interface IZabbixClient {

    void destroy();

    JsonNode call(ZabbixRequest request);

    ZabbixUser login();
}
