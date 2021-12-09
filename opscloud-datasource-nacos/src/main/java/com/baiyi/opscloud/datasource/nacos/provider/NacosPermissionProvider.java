package com.baiyi.opscloud.datasource.nacos.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.SingleTaskConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.nacos.convert.NacosPermissionConvert;
import com.baiyi.opscloud.datasource.nacos.entity.NacosPermission;
import com.baiyi.opscloud.datasource.nacos.drive.NacosAuthDrive;
import com.baiyi.opscloud.datasource.nacos.param.NacosPageParam;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/12 4:13 下午
 * @Version 1.0
 */
@Component
public class NacosPermissionProvider extends BaseAssetProvider<NacosPermission.Permission> {

    @Resource
    private NacosPermissionProvider nacosPermissionProvider;

    @Resource
    private NacosAuthDrive nacosAuthDrive;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NACOS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.NACOS_PERMISSION.name();
    }

    private NacosConfig.Nacos buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, NacosConfig.class).getNacos();
    }

    @Override
    protected List<NacosPermission.Permission> listEntities(DsInstanceContext dsInstanceContext) {
        try {
            NacosPermission.PermissionsResponse permissionsResponse = nacosAuthDrive.listPermissions(buildConfig(dsInstanceContext.getDsConfig()), NacosPageParam.PageQuery.builder().build());
            return permissionsResponse.getPageItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("查询条目失败");
    }

    @Override
    @SingleTask(name = SingleTaskConstants.PULL_NACOS_PERMISSION, lockTime = "1m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, NacosPermission.Permission entity) {
        return NacosPermissionConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(nacosPermissionProvider);
    }

}
