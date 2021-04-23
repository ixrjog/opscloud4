package com.baiyi.opscloud.zabbix.param;

import com.baiyi.opscloud.common.util.JSONUtils;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/2 10:51 上午
 * @Version 1.0
 */
public class ZabbixDefaultParam {

    private Map<String, String> params = Maps.newHashMap();

    public void putParam(String key, String value) {
        this.params.put(key, value);
    }

    @Override
    public String toString() {
        return JSONUtils.writeValueAsString(this);
    }
}
