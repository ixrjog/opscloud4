package com.baiyi.opscloud.zabbix.v50.request;

import com.baiyi.opscloud.zabbix.http.IZabbixRequest;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/17 11:27 上午
 * @Version 1.0
 */
public class ZabbixRequest {

    @Data
    @Builder
    public static class DefaultRequest implements IZabbixRequest {
        @Builder.Default
        private String jsonrpc = "2.0";
        @Builder.Default
        private Map<String, Object> params = Maps.newHashMap();
        private String method;
        private String auth;
        private Integer id;
        public void putParam(String key, Object value) {
            params.put(key, value);
        }
    }

}
