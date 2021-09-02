package com.baiyi.opscloud.task;

import com.baiyi.opscloud.ansible.provider.AnsibleHostsProvider;
import com.baiyi.opscloud.common.topic.TopicHelper;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetSubscriptionFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetSubscriptionService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.task.base.BaseTask;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
public class AssetSubscriptionTask extends BaseTask {

    @Resource
    private TopicHelper topicHelper;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private AnsibleHostsProvider ansibleHostsProvider;

    @Resource
    private DsInstanceAssetSubscriptionService dsInstanceAssetSubscriptionService;

    @Resource
    private DsInstanceAssetSubscriptionFacade dsInstanceAssetSubscriptionFacade;

    /**
     * By setting lockAtMostFor we make sure that the lock is released even if the node dies and by setting
     * `lockAtLeastFor` we make sure it's not executed more than once in fifteen minutes. Please note that
     * `lockAtMostFor` is just a safety net in case that the node executing the task dies, so set it to a time
     * that is significantly larger than maximum estimated execution time. If the task takes longer than lockAtMostFor,
     * it may be executed again and the results will be unpredictable (more processes will hold the lock).
     */
    @Scheduled(initialDelay = 5000, fixedRate = 60 * 1000)
    @SchedulerLock(name = "asset_subscription_task", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void assetSubscriptionTask() {
        log.info("资产订阅任务");
        if (!isHealth()) return;
        if (topicHelper.receive(TopicHelper.Topics.ASSET_SUBSCRIPTION_TASK) == null) return;
        log.info("定时任务开始: 资产订阅！");
        doTask();
        log.info("定时任务结束: 资产订阅！");
    }

    private void doTask() {
        List<DatasourceInstance> instances = dsInstanceService.listByInstanceType(DsTypeEnum.ANSIBLE.name());
        if (CollectionUtils.isEmpty(instances)) return;
        instances.forEach(i -> {
            List<DatasourceInstanceAsset> assets = dsInstanceAssetService.listByInstanceAssetType(i.getUuid(), DsAssetTypeEnum.ANSIBLE_HOSTS.name());
            if (CollectionUtils.isEmpty(assets)) return;
            ansibleHostsProvider.pullAsset(i.getId());
            publish(i.getId());
        });
    }

    private void publish(int assetId) {
        List<DatasourceInstanceAssetSubscription> assetSubscriptions = dsInstanceAssetSubscriptionService.queryByAssetId(assetId);
        if (CollectionUtils.isEmpty(assetSubscriptions)) return;
        assetSubscriptions.forEach(e ->
                dsInstanceAssetSubscriptionFacade.publishAssetSubscriptionById(e.getId())
        );
    }
}
