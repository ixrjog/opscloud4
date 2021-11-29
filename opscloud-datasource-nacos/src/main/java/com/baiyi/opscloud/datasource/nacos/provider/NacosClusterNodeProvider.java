package com.baiyi.opscloud.datasource.nacos.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.datasource.NacosConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.common.constant.SingleTaskConstants;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.nacos.convert.NacosClusterNodeConvert;
import com.baiyi.opscloud.datasource.nacos.entity.NacosCluster;
import com.baiyi.opscloud.datasource.nacos.drive.NacosClusterDrive;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/12 1:59 下午
 * @Version 1.0
 */
@Component
public class NacosClusterNodeProvider extends BaseAssetProvider<NacosCluster.Node> {

    @Resource
    private NacosClusterNodeProvider nacosClusterNodeProvider;

    @Resource
    private NacosClusterDrive nacosClusterDrive;

    @Override
    public String getInstanceType() {
        return DsTypeEnum.NACOS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.NACOS_CLUSTER_NODE.name();
    }

    private NacosConfig.Nacos buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, NacosConfig.class).getNacos();
    }

    @Override
    protected List<NacosCluster.Node> listEntities(DsInstanceContext dsInstanceContext) {

        try {
            NacosCluster.NodesResponse nodesResponse = nacosClusterDrive.listNodes(buildConfig(dsInstanceContext.getDsConfig()));
            if (nodesResponse.getCode() == 200) {
                return nodesResponse.getData();
            } else {
                return Collections.EMPTY_LIST;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("查询条目失败");
    }

    @Override
    @SingleTask(name = SingleTaskConstants.PULL_NACOS_CLUSTER_NODE, lockTime = "1m")
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
    protected AssetContainer toAssetContainer(DatasourceInstance dsInstance, NacosCluster.Node entity) {
        return NacosClusterNodeConvert.toAssetContainer(dsInstance, entity);
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(nacosClusterNodeProvider);
    }
}
