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
import com.baiyi.opscloud.datasource.nacos.entity.NacosRole;
import com.baiyi.opscloud.datasource.nacos.param.NacosPageParam;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/15 3:40 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class NacosUserProvider extends BaseAssetProvider<NacosRole.Role> {

    @Resource
    private NacosUserProvider nacosUserProvider;

    @Resource
    private NacosAuthDriver nacosAuthDriver;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NACOS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.NACOS_USER.name();
    }

    private NacosConfig.Nacos buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, NacosConfig.class).getNacos();
    }

    @Override
    protected List<NacosRole.Role> listEntities(DsInstanceContext dsInstanceContext) {
        try {
            NacosPageParam.PageQuery pageQuery = NacosPageParam.PageQuery.builder()
                    .pageSize(100)
                    .build();
            List<NacosRole.Role> entities = Lists.newArrayList();
            while (true) {
                NacosRole.RolesResponse rolesResponse = nacosAuthDriver.listRoles(buildConfig(dsInstanceContext.getDsConfig()), pageQuery);
                entities.addAll(rolesResponse.getPageItems());
                if (rolesResponse.getPagesAvailable() >= rolesResponse.getPageNumber()) {
                    break;
                }
                pageQuery.setPageNo(pageQuery.getPageNo() + 1);
            }
            return entities;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DatasourceProviderException(e.getMessage());
        }
    }

    @Override
    @SingleTask(name = SingleTaskConstants.PULL_NACOS_USER, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfKey2()
                .compareOfActive()
                .build();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(nacosUserProvider);
    }

}