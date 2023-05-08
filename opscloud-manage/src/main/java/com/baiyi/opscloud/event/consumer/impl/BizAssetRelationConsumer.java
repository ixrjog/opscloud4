package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.event.consumer.impl.kind.EmployeeResignConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/3/4 3:48 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class BizAssetRelationConsumer extends AbstractEventConsumer<BusinessAssetRelation> {

    private final EmployeeResignConsumer employeeResignConsumer;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.BUSINESS_ASSET_RELATION.name();
    }

    @Override
    protected void preHandle(NoticeEvent<BusinessAssetRelation> noticeEvent) {
        // TODO
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<BusinessAssetRelation> noticeEvent) {
        // TODO
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<BusinessAssetRelation> noticeEvent) {
        // TODO
    }

    @Override
    protected void onDeletedMessage(NoticeEvent<BusinessAssetRelation> noticeEvent) {
        BusinessAssetRelation eventData = toEventData(noticeEvent.getMessage());
        if (eventData.getBusinessType() == BusinessTypeEnum.USER.getType()) {
            employeeResignConsumer.consume(eventData);
        }
    }

}
