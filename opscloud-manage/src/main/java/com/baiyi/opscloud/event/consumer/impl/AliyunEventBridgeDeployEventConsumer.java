package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunEventBridgeConfig;
import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.driver.AliyunEventBridgeHookDriver;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.AliyunEventBridgeResult;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.CloudEvents;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.LeoDeployEvent;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.provider.AliyunEventBridgeDeployEventProvider;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/8/31 09:44
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunEventBridgeDeployEventConsumer extends AbstractEventConsumer<LeoHook.DeployHook> {

    private final AliyunEventBridgeHookDriver aliyunEventBridgeHookDriver;

    private final AliyunEventBridgeDeployEventProvider aliyunEventBridgeDeployEventProvider;

    private final InstanceHelper instanceHelper;

    private final DsConfigManager dsConfigManager;

    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.ALIYUN_EVENTBRIDGE};

    protected DsTypeEnum[] getFilterInstanceTypes() {
        return FILTER_INSTANCE_TYPES;
    }

    protected String getTag() {
        return TagConstants.NOTICE.getTag();
    }

    protected List<DatasourceInstance> listInstance() {
        return instanceHelper.listInstance(getFilterInstanceTypes(), getTag());
    }

    @Override
    public String getEventType() {
        return BusinessTypeEnum.EVENT_BRIDGE_DEPLOY_EVENT.name();
    }

    private AliyunEventBridgeConfig.EventBridge buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunEventBridgeConfig.class).getEventBridge();
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<LeoHook.DeployHook> noticeEvent) {
        LeoHook.DeployHook deployHook = toEventData(noticeEvent.getMessage());
        List<DatasourceInstance> instances = listInstance();
        for (DatasourceInstance instance : instances) {
            DatasourceConfig dsConfig = dsConfigManager.getConfigById(instance.getConfigId());
            AliyunEventBridgeConfig.EventBridge eventBridgeConfig = buildConfig(dsConfig);
            AliyunEventBridgeResult.Result result = null;
            boolean success = false;
            final String eventId = IdUtil.buildUUID();
            CloudEvents.Event event = CloudEvents.Event.builder()
                    .id(eventId)
                    .type(Optional.ofNullable(eventBridgeConfig).map(AliyunEventBridgeConfig.EventBridge::getLeo).map(AliyunEventBridgeConfig.Leo::getType).orElse(""))
                    .source(Optional.ofNullable(eventBridgeConfig).map(AliyunEventBridgeConfig.EventBridge::getLeo).map(AliyunEventBridgeConfig.Leo::getSource).orElse(""))
                    .specversion(Optional.ofNullable(eventBridgeConfig).map(AliyunEventBridgeConfig.EventBridge::getLeo).map(AliyunEventBridgeConfig.Leo::getSpecversion).orElse("1.0"))
                    .data(deployHook)
                    .build();
            try {
                result = aliyunEventBridgeHookDriver.publish(eventBridgeConfig, event);
                success = true;
            } catch (MalformedURLException | FeignException | OCException e) {
                log.error(e.getMessage());
            }
            LeoDeployEvent leoDeployEvent = LeoDeployEvent.builder()
                    .eventId(eventId)
                    .deployId(deployHook.getId())
                    .buildId(deployHook.getBuildId())
                    .name(deployHook.getName())
                    .hook(deployHook)
                    .success(success)
                    .requestId(success ? result.getResponse().getRequestId() : "")
                    .build();
            aliyunEventBridgeDeployEventProvider.pullAsset(instance.getId(), leoDeployEvent);
        }
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<LeoHook.DeployHook> noticeEvent) {
        this.onCreatedMessage(noticeEvent);
    }

}