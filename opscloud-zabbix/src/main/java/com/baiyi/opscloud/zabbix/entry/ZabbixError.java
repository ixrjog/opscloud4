package com.baiyi.opscloud.zabbix.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

/**
 * @Author baiyi
 * @Date 2020/12/3 3:51 下午
 * @Version 1.0
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZabbixError {

    /**
     * {
     *     "jsonrpc": "2.0",
     *     "error": {
     *         "code": -32602,
     *         "message": "Invalid params.",
     *         "data": "No groups for host \"Linux server\"."
     *     },
     *     "id": 7
     * }
     */

    private Integer code;
    private String message;
    private String data;


}