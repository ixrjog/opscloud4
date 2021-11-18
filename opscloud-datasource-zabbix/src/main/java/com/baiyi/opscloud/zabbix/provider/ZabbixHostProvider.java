package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.zabbix.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.provider.base.BaseZabbixHostProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:17 下午
 * @Since 1.0
 */

@Component
public class ZabbixHostProvider extends BaseZabbixHostProvider<ZabbixHostGroup> {

    @Resource
    private ZabbixHostProvider zabbixHostProvider;

    @Override
    protected List<ZabbixHost> listEntities(DsInstanceContext dsInstanceContext, ZabbixHostGroup target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixHostHandler.getByGroup(zabbix, target);
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ZABBIX_HOST_GROUP.getType();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixHostProvider);
    }
}
