package com.baiyi.opscloud.schedule.task;

import com.baiyi.opscloud.datasource.ansible.provider.AnsibleHostsProvider;
import com.baiyi.opscloud.common.helper.TopicHelper;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.annotation.InstanceHealth;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetSubscriptionFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetSubscriptionService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.schedule.task.base.AbstractTask;
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
public class AssetSubscriptionTask extends AbstractTask {

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

    @InstanceHealth // 实例健康检查，高优先级
    @Scheduled(initialDelay = 5000, fixedRate = 60 * 1000)
    /**
     * By setting lockAtMostFor we make sure that the lock is released even if the node dies and by setting
     * `lockAtLeastFor` we make sure it's not executed more than once in fifteen minutes. Please note that
     * `lockAtMostFor` is just a safety net in case that the node executing the task dies, so set it to a time
     * that is significantly larger than maximum estimated execution time. If the task takes longer than lockAtMostFor,
     * it may be executed again and the results will be unpredictable (more processes will hold the lock).
     */
    @SchedulerLock(name = "asset_subscription_task", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void assetSubscriptionTask() {
        if (receive(TopicHelper.Topics.ASSET_SUBSCRIPTION_TASK) == null) return;
        log.info("定时任务开始: 资产订阅！");
        doTask();
        log.info("定时任务结束: 资产订阅！");
    }

    private void doTask() {
        List<DatasourceInstance> instances = dsInstanceService.listByInstanceType(DsTypeEnum.ANSIBLE.name());
        if (CollectionUtils.isEmpty(instances)) return;
        instances.forEach(i -> {
            List<DatasourceInstanceAsset> assets = dsInstanceAssetService.listByInstanceAssetType(i.getUuid(), DsAssetTypeConstants.ANSIBLE_HOSTS.name());
            if (CollectionUtils.isEmpty(assets)) return;
            log.info("构建Ansible主机清单文件！");
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
        if (CollectionUtils.isEmpty(assetSubscriptions)) return;
        assetSubscriptions.forEach(e -> {
                    log.info("发布订阅配置: assetSubscriptionId = {}", e.getId());
                    dsInstanceAssetSubscriptionFacade.publishAssetSubscription(e);
                }
        );
    }
}
