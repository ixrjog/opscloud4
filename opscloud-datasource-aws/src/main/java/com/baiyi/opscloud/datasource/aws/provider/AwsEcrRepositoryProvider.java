package com.baiyi.opscloud.datasource.aws.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aws.ecr.delegate.AmazonEcrDelegate;
import com.baiyi.opscloud.datasource.aws.ecr.entity.AmazonEcr;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    protected AssetComparer getAssetComparer() {
        return AssetComparerBuilder.newBuilder()
                .compareOfName()
                .compareOfKey2()
                .compareOfKind()
                .build();
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