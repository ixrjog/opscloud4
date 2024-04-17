package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunEventBridgeConfig;
import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.entity.InterceptRelease;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.driver.AliyunEventBridgeHookDriver;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.AliyunEventBridgeResult;
import com.baiyi.opscloud.datasource.aliyun.eventbridge.entity.CloudEvents;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/4/9 下午2:05
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunEventBridgeApolloReleaseEventConsumer extends AbstractEventConsumer<InterceptRelease.Event> {

    private final AliyunEventBridgeHookDriver aliyunEventBridgeHookDriver;

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
        return BusinessTypeEnum.EVENT_BRIDGE_APOLLO_RELEASE_EVENT.name();
    }

    private AliyunEventBridgeConfig.EventBridge buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunEventBridgeConfig.class).getEventBridge();
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<InterceptRelease.Event> noticeEvent) {
        InterceptRelease.Event eventData = toEventData(noticeEvent.getMessage());
        List<DatasourceInstance> instances = listInstance();
        for (DatasourceInstance instance : instances) {
            DatasourceConfig dsConfig = dsConfigManager.getConfigById(instance.getConfigId());
            AliyunEventBridgeConfig.EventBridge eventBridgeConfig = buildConfig(dsConfig);
            AliyunEventBridgeResult.Result result = null;
            boolean success = false;
            final String eventId = IdUtil.buildUUID();
            CloudEvents.ApolloEvent event = CloudEvents.ApolloEvent.builder()
                    .id(eventId)
                    .type(Optional.ofNullable(eventBridgeConfig).map(AliyunEventBridgeConfig.EventBridge::getApollo).map(AliyunEventBridgeConfig.Config::getType).orElse(""))
                    .source(Optional.ofNullable(eventBridgeConfig).map(AliyunEventBridgeConfig.EventBridge::getApollo).map(AliyunEventBridgeConfig.Config::getSource).orElse(""))
                    .specversion(Optional.ofNullable(eventBridgeConfig).map(AliyunEventBridgeConfig.EventBridge::getApollo).map(AliyunEventBridgeConfig.Config::getSpecversion).orElse("1.0"))
                    .data(eventData)
                    .build();
            try {
                result = aliyunEventBridgeHookDriver.publish(eventBridgeConfig, event);
                success = true;
            } catch (MalformedURLException | FeignException | OCException e) {
                log.error(e.getMessage());
            }

        }
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<InterceptRelease.Event> noticeEvent) {
        this.onCreatedMessage(noticeEvent);
    }

}