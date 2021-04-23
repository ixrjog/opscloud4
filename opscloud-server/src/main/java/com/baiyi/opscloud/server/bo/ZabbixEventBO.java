package com.baiyi.opscloud.server.bo;

import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import lombok.Builder;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/20 4:03 下午
 * @Since 1.0
 */
public class ZabbixEventBO {

    @Data
    @Builder
    public static class HostUpdate {

        private ZabbixHost host;

        private OcServer ocServer;
    }
}