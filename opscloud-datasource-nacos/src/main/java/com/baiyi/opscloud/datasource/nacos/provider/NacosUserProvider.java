package com.baiyi.opscloud.datasource.nacos.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constant.SingleTaskConstants;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.nacos.convert.NacosRoleConvert;
import com.baiyi.opscloud.datasource.nacos.entry.NacosRole;
import com.baiyi.opscloud.datasource.nacos.datasource.NacosAuthDatasource;
import com.baiyi.opscloud.datasource.nacos.param.NacosPageParam;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/15 3:40 下午
 * @Version 1.0
 */
@Component
public class NacosUserProvider extends BaseAssetProvider<NacosRole.Role> {

    @Resource
    private NacosUserProvider nacosUserProvider;

    @Resource
    private NacosAuthDatasource nacosAuthDatasource;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NACOS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.NACOS_USER.name();
    }

    private NacosConfig.Nacos buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, NacosConfig.class).getNacos();
    }

    @Override
    protected List<NacosRole.Role> listEntries(DsInstanceContext dsInstanceContext) {
        try {
            NacosPageParam.PageQuery pageQuery = NacosPageParam.PageQuery.builder()
                    .pageSize(100)
                    .build();
            List<NacosRole.Role> entries = Lists.newArrayList();
            while (true) {
                NacosRole.RolesResponse rolesResponse = nacosAuthDatasource.listRoles(buildConfig(dsInstanceContext.getDsConfig()), pageQuery);
                entries.addAll(rolesResponse.getPageItems());
                if (rolesResponse.getPagesAvailable() >= rolesResponse.getPageNumber())
                    break;
                pageQuery.setPageNo(pageQuery.getPageNo() + 1);
            }
            return entries;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("查询条目失败");
    }

    @Override
    @SingleTask(name = SingleTaskConstants.PULL_NACOS_USER, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
//        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
//            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (preAsset.getIsActive() != asset.getIsActive())
            return false;
        return true;
    }

    @Override
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, NacosRole.Role entry) {
        return NacosRoleConvert.toAssetContainer(dsInstance, entry);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(nacosUserProvider);
    }

}
