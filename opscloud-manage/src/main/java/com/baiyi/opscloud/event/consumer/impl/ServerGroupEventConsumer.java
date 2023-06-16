package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.helper.topic.TopicHelper;
import com.baiyi.opscloud.datasource.ansible.ServerGroupingAlgorithm;
import com.baiyi.opscloud.datasource.manager.DsServerGroupManager;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.util.ServerTreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/18 10:17 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServerGroupEventConsumer extends AbstractEventConsumer<ServerGroup> {

    private final ServerTreeUtil serverTreeUtil;

    private final ServerGroupingAlgorithm serverGroupingAlgorithm;

    private final DsServerGroupManager dsServerGroupManager;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.SERVERGROUP.name();
    }

    @Override
    protected void preHandle(NoticeEvent<ServerGroup> noticeEvent) {
        ServerGroup eventData = toEventData(noticeEvent.getMessage());
        serverGroupingAlgorithm.evictGrouping(eventData.getId());
        serverGroupingAlgorithm.evictIntactGrouping(eventData.getId(),true);
        serverGroupingAlgorithm.evictIntactGrouping(eventData.getId(),false);
        serverTreeUtil.evictWrap(eventData.getId());
        topicHelper.send(TopicHelper.Topics.ASSET_SUBSCRIPTION_TASK, 1);
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<ServerGroup> noticeEvent) {
        ServerGroup eventData = toEventData(noticeEvent.getMessage());
        dsServerGroupManager.create(eventData);
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<ServerGroup> noticeEvent) {
        ServerGroup eventData = toEventData(noticeEvent.getMessage());
        dsServerGroupManager.update(eventData);
    }

}
