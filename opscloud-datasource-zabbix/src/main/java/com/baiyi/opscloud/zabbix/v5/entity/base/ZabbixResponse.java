package com.baiyi.opscloud.zabbix.v5.entity.base;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/11/18 1:54 下午
 * @Version 1.0
 */
public class ZabbixResponse {

    @Data
    public static class Response {
        private String jsonrpc;
        private Integer id;
        private Error error;
    }

    @Data
    public static class Error {
        private Integer code;
        private String message;
        private String data;
    }

}