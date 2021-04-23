package com.baiyi.opscloud.zabbix.param;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/2/1 10:53 上午
 * @Version 1.0
 */
public class ZabbixHostgroupParam {

    @Data
    @Builder
    public static class Hostgroup {
        private String groupid;
        private String name;
        private String internal;
        private String flags;
    }
}
