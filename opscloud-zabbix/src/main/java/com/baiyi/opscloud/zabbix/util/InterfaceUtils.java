package com.baiyi.opscloud.zabbix.util;

import com.baiyi.opscloud.zabbix.base.InterfaceType;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/2/19 10:05 上午
 * @Version 1.0
 */
public class InterfaceUtils {

    public static ZabbixHostInterface filter(List<ZabbixHostInterface> interfaces, InterfaceType interfaceType) {
        try {
            return interfaces.stream().filter(i ->
                    i.getType().equals(interfaceType.getType())
            ).collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
