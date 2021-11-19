package com.baiyi.opscloud.zabbix.v5.request;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/11/18 4:35 下午
 * @Version 1.0
 */
@Data
@Builder
public class ZabbixDeleteRequest implements IZabbixRequest {

    @Builder.Default
    private String jsonrpc = "2.0";
    private String method;
    private String auth;
    @Builder.Default
    private Integer id = 1;
    private String[] params;

}