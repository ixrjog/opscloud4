package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.zabbix.provider.base.AbstractZabbixHostProvider;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/7/1 2:17 下午
 * @Since 1.0
 */

@Component
public class ZabbixHostProvider extends AbstractZabbixHostProvider<ZabbixHostGroup.HostGroup> {

    @Resource
    private ZabbixHostProvider zabbixHostProvider;

    @Override
    protected List<com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost.Host> listEntities(DsInstanceContext dsInstanceContext, com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup.HostGroup target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixV5HostDrive.getByGroup(zabbix, target);
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeConstants.ZABBIX_HOST_GROUP.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixHostProvider);
    }

}