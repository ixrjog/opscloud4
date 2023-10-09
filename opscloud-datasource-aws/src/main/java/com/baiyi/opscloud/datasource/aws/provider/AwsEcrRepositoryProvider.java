package com.baiyi.opscloud.datasource.aws.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.aws.ecr.delegate.AmazonEcrDelegate;
import com.baiyi.opscloud.datasource.aws.ecr.entity.AmazonEcr;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_AWS_ECR_REPOSITORY;

/**
 * @Author baiyi
 * @Date 2022/8/16 18:28
 * @Version 1.0
 */
@Slf4j
@Component
public class AwsEcrRepositoryProvider extends BaseAssetProvider<AmazonEcr.Repository> {

    @Resource
    private AmazonEcrDelegate amazonEcrDelegate;

    @Resource
    private AwsEcrRepositoryProvider awsEcrRepositoryProvider;

    @Override
    @SingleTask(name = PULL_AWS_ECR_REPOSITORY, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }

    private AwsConfig.Aws buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AwsConfig.class).getAws();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName())) {
            return false;
        }
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2())) {
            return false;
        }
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind())) {
            return false;
        }
        return true;
    }

    @Override
    protected List<AmazonEcr.Repository> listEntities(DsInstanceContext dsInstanceContext) {
        AwsConfig.Aws aws = buildConfig(dsInstanceContext.getDsConfig());
        return amazonEcrDelegate.listRepository(aws);
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ECR_REPOSITORY.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(awsEcrRepositoryProvider);
    }

}