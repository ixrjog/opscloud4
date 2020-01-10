package com.baiyi.opscloud.zabbix.http;

import com.baiyi.opscloud.common.util.JSONUtils;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 5:51 下午
 * @Version 1.0
 */
@Data
public class ZabbixRequestParamsMap extends ZabbixRequest {

    private Map<String, Object> params = Maps.newHashMap();

    public void putParam(String key, Object value) {
        this.params.put(key, value);
    }

    @Override
    public String toString() {
        return JSONUtils.writeValueAsString(this);
    }
}
