package com.baiyi.opscloud.zabbix.http;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/23 2:00 下午
 * @Since 1.0
 */
@Data
public class ZabbixFilter {

    private Map<String, Object> filter = Maps.newHashMap();

    public void put(String key, Object value) {
        this.filter.put(key, value);
    }
}
