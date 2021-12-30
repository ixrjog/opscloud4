package com.baiyi.opscloud.datasource.business.server.util;

import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.zabbix.v5.param.ZabbixHostParam;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/12/30 4:32 PM
 * @Version 1.0
 */
public class HostParamUtil {

    private HostParamUtil(){}

    public static ZabbixHostParam.Interface buildInterfaceParam(Server server, ServerProperty.Server property) {
        return ZabbixHostParam.Interface.builder()
                .ip(getManageIp(server, property))
                .build();
    }

    public static String getManageIp(Server server, ServerProperty.Server property) {
        String manageIp = Optional.ofNullable(property)
                .map(ServerProperty.Server::getMetadata)
                .map(ServerProperty.Metadata::getManageIp)
                .orElse(server.getPrivateIp());
        return StringUtils.isEmpty(manageIp) ? server.getPrivateIp() : manageIp;
    }

    public static List<ZabbixHostParam.Template> toTemplateParam(List<com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate.Template> templates) {
        return templates.stream().map(e -> ZabbixHostParam.Template.builder().templateid(e.getTemplateid()).build())
                .collect(Collectors.toList());
    }

}
