package com.baiyi.opscloud.event.customer.impl;

import com.baiyi.opscloud.datasource.ansible.ServerGroupingAlgorithm;
import com.baiyi.opscloud.common.topic.TopicHelper;
import com.baiyi.opscloud.datasource.manager.DsServerGroupManager;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.event.NoticeEvent;
import com.baiyi.opscloud.util.ServerTreeUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/18 10:17 上午
 * @Version 1.0
 */
@Component
public class ServerGroupEventCustomer extends AbstractEventConsumer<ServerGroup> {

    @Resource
    private ServerTreeUtil serverTreeUtil;

    @Resource
    private ServerGroupingAlgorithm serverGroupingAlgorithm;

    @Resource
    private DsServerGroupManager dsServerGroupManager;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.SERVERGROUP.name();
    }

    @Override
    protected void preEventHandle(NoticeEvent noticeEvent) {
        ServerGroup eventData = toEventData(noticeEvent.getMessage());
        serverGroupingAlgorithm.evictGrouping(eventData.getId());
        serverGroupingAlgorithm.evictIntactGrouping(eventData.getId(),true);
        serverGroupingAlgorithm.evictIntactGrouping(eventData.getId(),false);
        serverTreeUtil.evictWrap(eventData.getId());
        topicHelper.send(TopicHelper.Topics.ASSET_SUBSCRIPTION_TASK, 1);
    }

    @Override
    protected void onCreateMessage(NoticeEvent noticeEvent) {
        ServerGroup eventData = toEventData(noticeEvent.getMessage());
        dsServerGroupManager.create(eventData);
    }

    @Override
    protected void onUpdateMessage(NoticeEvent noticeEvent) {
        ServerGroup eventData = toEventData(noticeEvent.getMessage());
        dsServerGroupManager.update(eventData);
    }


}
