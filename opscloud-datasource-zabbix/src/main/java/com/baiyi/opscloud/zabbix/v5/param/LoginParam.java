package com.baiyi.opscloud.zabbix.v5.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/17 10:36 上午
 * @Version 1.0
 */
public class LoginParam {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class LoginRequest {

        private final String jsonrpc = "2.0";
        private String method;
        private Map<String, Object> params;
        private Integer id;
        private String auth;

    }

}