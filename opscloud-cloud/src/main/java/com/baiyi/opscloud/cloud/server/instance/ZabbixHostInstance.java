package com.baiyi.opscloud.cloud.server.instance;

import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2019/11/29 2:37 PM
 * @Version 1.0
 */
@Builder
@Data
public class ZabbixHostInstance  {

    private ZabbixHost host;
    private List<ZabbixHostInterface> interfaceList;

}
