package com.baiyi.opscloud.datasource.aws.provider;

import com.amazonaws.services.sns.model.Subscription;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.comparer.AssetComparerBuilder;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetBusinessRelationProvider;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleNotificationServiceDriver;
import com.baiyi.opscloud.datasource.aws.sqs.entity.SimpleNotificationService;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_AWS_SNS_SUBSCRIPTION;

/**
 * @Author baiyi
 * @Date 2022/3/29 16:52
 * @Version 1.0
 */
@Slf4j
@Component
public class AwsSnsSubscriptionProvider extends AbstractAssetBusinessRelationProvider<SimpleNotificationService.Subscription> {

    @Resource
    private AmazonSimpleNotificationServiceDriver amazonSNSDriver;

    @Resource
    private AwsSnsSubscriptionProvider awsSnsSubscriptionProvider;

    @Override
    @SingleTask(name = PULL_AWS_SNS_SUBSCRIPTION, lockTime = "1m")
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
    protected List<SimpleNotificationService.Subscription> listEntities(DsInstanceContext dsInstanceContext) {
        AwsConfig.Aws aws = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aws.getRegionIds())) {
            return Collections.emptyList();
        }
        List<SimpleNotificationService.Subscription> entities = Lists.newArrayList();
        aws.getRegionIds().forEach(regionId -> {
                    List<Subscription> subscriptions = amazonSNSDriver.listSubscriptions(aws, regionId);
                    entities.addAll(subscriptions.stream().map(e ->
                            SimpleNotificationService.Subscription.builder()
                                    .subscriptionArn(e.getSubscriptionArn())
                                    .topicArn(e.getTopicArn())
                                    .endpoint(e.getEndpoint())
                                    .protocol(e.getProtocol())
                                    .regionId(regionId)
                                    .build()
                    ).toList());
                }
        );
        return entities;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.SNS_SUBSCRIPTION.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(awsSnsSubscriptionProvider);
    }

}

