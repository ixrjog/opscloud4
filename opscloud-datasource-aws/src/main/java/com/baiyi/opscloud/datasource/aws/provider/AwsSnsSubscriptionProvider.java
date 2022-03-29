package com.baiyi.opscloud.datasource.aws.provider;

import com.amazonaws.services.sns.model.Subscription;
import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.AbstractAssetBusinessRelationProvider;
import com.baiyi.opscloud.core.util.AssetUtil;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleNotificationServiceDriver;
import com.baiyi.opscloud.datasource.aws.sqs.entity.SimpleNotificationService;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        return dsConfigHelper.build(dsConfig, AwsConfig.class).getAws();
    }

    @Override
    protected boolean equals(DatasourceInstanceAsset asset, DatasourceInstanceAsset preAsset) {
        if (!AssetUtil.equals(preAsset.getName(), asset.getName()))
            return false;
        if (!AssetUtil.equals(preAsset.getAssetKey2(), asset.getAssetKey2()))
            return false;
        if (!AssetUtil.equals(preAsset.getKind(), asset.getKind()))
            return false;
//        if (!AssetUtil.equals(preAsset.getDescription(), asset.getDescription()))
//            return false;
//        if (!AssetUtil.equals(preAsset.getCreatedTime(), asset.getCreatedTime()))
//            return false;
        return true;
    }

    @Override
    protected List<SimpleNotificationService.Subscription> listEntities(DsInstanceContext dsInstanceContext) {
        AwsConfig.Aws aws = buildConfig(dsInstanceContext.getDsConfig());
        if (CollectionUtils.isEmpty(aws.getRegionIds()))
            return Collections.emptyList();
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
                    ).collect(Collectors.toList()));
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

