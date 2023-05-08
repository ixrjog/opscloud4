package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.facade.leo.LeoDeployFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/8 14:22
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AutoDeployEventConsumer extends AbstractEventConsumer<LeoDeployParam.DoAutoDeploy> {

    private final LeoDeployFacade leoDeployFacade;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.LEO_AUTO_DEPLOY.name();
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<LeoDeployParam.DoAutoDeploy> noticeEvent) {
        LeoDeployParam.DoAutoDeploy doAutoDeploy = toEventData(noticeEvent.getMessage());
        leoDeployFacade.doAutoDeploy(doAutoDeploy);
    }

    @Override
    protected void onUpdatedMessage(NoticeEvent<LeoDeployParam.DoAutoDeploy> noticeEvent) {
      this.onCreatedMessage(noticeEvent);
    }

}
