package com.baiyi.opscloud.zabbix.v5.drive;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.zabbix.v5.drive.base.SimpleZabbixV5HostDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/29 3:06 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixV5HostMacroDrive extends SimpleZabbixV5HostDrive {

    public void updateHostMacro(ZabbixConfig.Zabbix config, ZabbixHost.Host host, ServerProperty.Zabbix zabbix) {
        List<ServerProperty.Macro> macros = zabbix.toMacros();
        if (CollectionUtils.isEmpty(macros)) return;

        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostid", host.getHostid())
                .putParam("macros", macros)
                .build();
        ZabbixHost.UpdateHostResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("更新Zabbix主机宏属性: hostName = {} , macros = {}", host.getHost(), JSONUtil.writeValueAsString(macros));
        }
    }

}
