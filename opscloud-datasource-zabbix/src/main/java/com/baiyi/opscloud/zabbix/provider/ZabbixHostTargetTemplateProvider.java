package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.zabbix.provider.base.AbstractZabbixHostProvider;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author 修远
 * @Date 2021/7/1 6:07 下午
 * @Since 1.0
 */

@Component
public class ZabbixHostTargetTemplateProvider extends AbstractZabbixHostProvider<ZabbixTemplate.Template> {

    @Resource
    private ZabbixHostTargetTemplateProvider zabbixHostTargetTemplateProvider;

    @Override
    protected List<com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost.Host> listEntities(DsInstanceContext dsInstanceContext, ZabbixTemplate.Template target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixV5HostDrive.listByTemplate(zabbix, target);
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.ZABBIX_TEMPLATE.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixHostTargetTemplateProvider);
    }

}