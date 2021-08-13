package com.baiyi.opscloud.zabbix.http;

import lombok.Builder;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/28 1:19 下午
 * @Since 1.0
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
