package com.baiyi.opscloud.zabbix.provider;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.provider.base.BaseZabbixHostProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 6:07 下午
 * @Since 1.0
 */

@Component
public class ZabbixHostTargetTemplateProvider extends BaseZabbixHostProvider<ZabbixTemplate> {

    @Resource
    private ZabbixHostTargetTemplateProvider zabbixHostTargetTemplateProvider;

    @Override
    protected List<ZabbixHost> listEntries(DsInstanceContext dsInstanceContext, ZabbixTemplate target) {
        ZabbixConfig.Zabbix zabbix = buildConfig(dsInstanceContext.getDsConfig());
        return zabbixHostHandler.listByTemplate(zabbix, target);
    }

    @Override
    public String getTargetAssetKey() {
        return DsAssetTypeEnum.ZABBIX_TEMPLATE.getType();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(zabbixHostTargetTemplateProvider);
    }
}
