package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.common.annotation.WatchTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.helper.topic.TopicHelper;
import com.baiyi.opscloud.configuration.condition.EnvCondition;
import com.baiyi.opscloud.datasource.ansible.provider.AnsibleHostsProvider;
import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetSubscriptionFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetSubscriptionService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 资产订阅任务
 *
 * @Author baiyi
 * @Date 2021/8/27 10:00 上午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Conditional(EnvCondition.class)
public class AssetSubscriptionTask {

    private final DsInstanceService dsInstanceService;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final AnsibleHostsProvider ansibleHostsProvider;

    private final DsInstanceAssetSubscriptionService dsInstanceAssetSubscriptionService;

    private final DsInstanceAssetSubscriptionFacade dsInstanceAssetSubscriptionFacade;

    private final TopicHelper topicHelper;

    protected Object receive() {
        return topicHelper.receive(TopicHelper.Topics.ASSET_SUBSCRIPTION_TASK);
    }

    @InstanceHealth
    @Scheduled(initialDelay = 5000, fixedRate = 60 * 1000)
    /*
      By setting lockAtMostFor we make sure that the lock is released even if the node dies and by setting
      `lockAtLeastFor` we make sure it's not executed more than once in fifteen minutes. Please note that
      `lockAtMostFor` is just a safety net in case that the node executing the task dies, so set it to a time
      that is significantly larger than maximum estimated execution time. If the task takes longer than lockAtMostFor,
      it may be executed again and the results will be unpredictable (more processes will hold the lock).
     */
    @SchedulerLock(name = "asset_subscription_task", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    @WatchTask(name = "Asset subscription")
    public void run() {
        if (receive() == null) {
            return;
        }
        task();
    }

    private void task() {
        List<DatasourceInstance> instances = dsInstanceService.listByInstanceType(DsTypeEnum.ANSIBLE.name());
        if (CollectionUtils.isEmpty(instances)) {
            return;
        }
        instances.forEach(i -> {
            List<DatasourceInstanceAsset> assets = dsInstanceAssetService.listByInstanceAssetType(i.getUuid(), DsAssetTypeConstants.ANSIBLE_HOSTS.name());
            if (CollectionUtils.isEmpty(assets)) {
                return;
            }
            log.info("构建Ansible主机清单文件: instanceId={}", i.getId());
            ansibleHostsProvider.pullAsset(i.getId());
            assets.forEach(this::publish);
        });
    }

    /**
     * 发布
     *
     * @param asset
     */
    private void publish(DatasourceInstanceAsset asset) {
        List<DatasourceInstanceAssetSubscription> assetSubscriptions = dsInstanceAssetSubscriptionService.queryByAssetId(asset.getId());
        if (CollectionUtils.isEmpty(assetSubscriptions)) {
            return;
        }
        assetSubscriptions.forEach(e -> {
                    log.info("发布订阅配置: assetSubscriptionId={}", e.getId());
                    dsInstanceAssetSubscriptionFacade.publishAssetSubscription(e);
                }
        );
    }

}