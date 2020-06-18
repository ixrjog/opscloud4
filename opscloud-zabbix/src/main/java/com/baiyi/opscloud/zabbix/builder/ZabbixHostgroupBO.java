package com.baiyi.opscloud.zabbix.builder;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/1/10 11:26 上午
 * @Version 1.0
 */
@Data
@Builder
public class ZabbixHostgroupBO {
    private String groupid;
    private String name;
    private String internal;
    private String flags;
}
