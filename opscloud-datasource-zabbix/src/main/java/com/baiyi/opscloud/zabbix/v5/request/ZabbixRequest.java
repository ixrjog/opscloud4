package com.baiyi.opscloud.zabbix.v5.request;

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

    public interface IZabbixRequest {
        void setMethod(String method);
        void setAuth(String auth);
        String getMethod();
        String getAuth();
    }

    @Data
    @Builder
    public static class DefaultRequest implements IZabbixRequest {

        private final String jsonrpc = "2.0";
        @Builder.Default
        private Map<String, Object> params = Maps.newHashMap();
        private String method;
        private String auth;
        private Integer id;

        public void putParam(String key, Object value) {
            params.put(key, value);
        }
    }

    @Data
    @Builder
    public static class DeleteRequest implements IZabbixRequest {

        private final String jsonrpc = "2.0";
        private String method;
        private String auth;
        @Builder.Default
        private Integer id = 1;
        private String[] params;
    }

    @Data
    @Builder
    public static class Filter {

        private final Map<String, Object> filter = Maps.newHashMap();
        public void put(String key, Object value) {
            this.filter.put(key, value);
        }
    }

}