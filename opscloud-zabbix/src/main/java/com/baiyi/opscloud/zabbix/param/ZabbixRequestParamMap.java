package com.baiyi.opscloud.zabbix.param;

import com.baiyi.opscloud.common.util.JSONUtils;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/1 2:50 下午
 * @Version 1.0
 */
@Data
public class ZabbixRequestParamMap extends ZabbixRequest {

    private Map<String, Object> params = Maps.newHashMap();

    public void putParam(String key, Object value) {
        this.params.put(key, value);
    }

    @Override
    public String toString() {
        return JSONUtils.writeValueAsString(this);
    }
}
