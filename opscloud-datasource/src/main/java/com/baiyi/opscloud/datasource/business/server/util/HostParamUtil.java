package com.baiyi.opscloud.datasource.business.server.util;

import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.service.business.BizPropertyHelper;
import com.baiyi.opscloud.zabbix.v5.param.ZabbixHostParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/12/30 4:32 PM
 * @Version 1.0
 */
public class HostParamUtil {

    private HostParamUtil() {
    }

    public static ZabbixHostParam.Interface buildInterfaceParam(Server server, ServerProperty.Server property) {
        return ZabbixHostParam.Interface.builder()
                .ip(getManageIp(server, property))
                .build();
    }

    public static String getManageIp(Server server, ServerProperty.Server property) {
        return BizPropertyHelper.getManageIp(server, property);
    }

    public static int getSshPort( ServerProperty.Server property) {
        return BizPropertyHelper.getSshPort(property);
    }

    public static List<ZabbixHostParam.Template> toTemplateParam(List<com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate.Template> templates) {
        return templates.stream().map(e -> ZabbixHostParam.Template.builder().templateid(e.getTemplateid()).build())
                .collect(Collectors.toList());
    }

}
