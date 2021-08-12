package com.baiyi.opscloud.zabbix.http;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/25 11:11 上午
 * @Since 1.0
 */

@Data
public class SimpleZabbixRequest implements ZabbixRequest{

    private String jsonrpc = "2.0";

    private Map<String, Object> params = Maps.newHashMap();

    private String method;

    private String auth;

    private Integer id;

    public void putParam(String key, Object value) {
        params.put(key, value);
    }

}
