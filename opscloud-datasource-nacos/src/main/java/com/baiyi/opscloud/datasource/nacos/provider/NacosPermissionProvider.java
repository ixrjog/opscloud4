package com.baiyi.opscloud.datasource.nacos.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.SingleTaskConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.exception.DatasourceProviderException;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.nacos.driver.NacosAuthDriver;
import com.baiyi.opscloud.datasource.nacos.entity.NacosPermission;
import com.baiyi.opscloud.datasource.nacos.param.NacosPageParam;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/12 4:13 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class NacosPermissionProvider extends BaseAssetProvider<NacosPermission.Permission> {

    @Resource
    private NacosPermissionProvider nacosPermissionProvider;

    @Resource
    private NacosAuthDriver nacosAuthDriver;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NACOS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.NACOS_PERMISSION.name();
    }

    private NacosConfig.Nacos buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, NacosConfig.class).getNacos();
    }

    @Override
    protected List<NacosPermission.Permission> listEntities(DsInstanceContext dsInstanceContext) {
        try {
            NacosPermission.PermissionsResponse permissionsResponse = nacosAuthDriver.listPermissions(buildConfig(dsInstanceContext.getDsConfig()), NacosPageParam.PageQuery.builder().build());
            return permissionsResponse.getPageItems();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    @SingleTask(name = SingleTaskConstants.PULL_NACOS_PERMISSION, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfActive()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(nacosPermissionProvider);
    }

}