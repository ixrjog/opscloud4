package com.baiyi.opscloud.zabbix.param;

import com.baiyi.opscloud.common.util.JSONUtils;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/1 5:42 下午
 * @Version 1.0
 */
@Data
public class ZabbixFilter {

    private Map<String, Object> filter = Maps.newHashMap();

    public void put(String key, Object value) {
        this.filter.put(key, value);
    }

    @Override
    public String toString() {
        return JSONUtils.writeValueAsString(this);
    }
}
