package com.baiyi.opscloud.zabbix.param;

import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/2/1 3:35 下午
 * @Version 1.0
 */
@Data
public class ZabbixRequest implements ZabbixBaseRequest {

    private String jsonrpc = "2.0";
    private String method;
    private String auth;
    private Integer id;

}
