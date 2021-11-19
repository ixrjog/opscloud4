package com.baiyi.opscloud.zabbix.http;

import com.baiyi.opscloud.zabbix.v5.request.IZabbixRequest;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 4:17 下午
 * @Since 1.0
 */
public interface IZabbixClient {

    void close();

    JsonNode call(IZabbixRequest request);

}
