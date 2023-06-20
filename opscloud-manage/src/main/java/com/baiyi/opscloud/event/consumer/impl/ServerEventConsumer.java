package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.common.helper.topic.TopicHelper;
import com.baiyi.opscloud.datasource.ansible.ServerGroupingAlgorithm;
import com.baiyi.opscloud.datasource.manager.DsServerManager;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.util.ServerTreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/17 7:03 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ServerEventConsumer extends AbstractEventConsumer<Server> {

    private final ServerTreeUtil serverTreeUtil;

    private final ServerGroupingAlgorithm serverGroupingAlgorithm;

    private final DsServerManager dsServerManager;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.SERVER.name();
    }

    @Override
    protected void preHandle(NoticeEvent<Server> noticeEvent) {
        Server eventData = toEventData(noticeEvent.getMessage());
        serverGroupingAlgorithm.evictGrouping(eventData.getServerGroupId());
        serverGroupingAlgorithm.evictIntactGrouping(eventData.getServerGroupId(), true);
        serverGroupingAlgorithm.evictIntactGrouping(eventData.getServerGroupId(), false);
        serverTreeUtil.evictWrap(eventData.getServerGroupId());
        // 发送Topic 定时任务延迟执行
        topicHelper.send(TopicHelper.Topics.ASSET_SUBSCRIPTION_TASK, 1);
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<Server> noticeEvent) {
        Server eventData = toEventData(noticeEvent.getMessage());
        dsServerManager.create(eventData);
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<Server> noticeEvent) {
        Server eventData = toEventData(noticeEvent.getMessage());
        dsServerManager.update(eventData);
    }

    @Override
    protected void onDeletedMessage(NoticeEvent<Server> noticeEvent) {
        Server eventData = toEventData(noticeEvent.getMessage());
        dsServerManager.delete(eventData);
    }

}
