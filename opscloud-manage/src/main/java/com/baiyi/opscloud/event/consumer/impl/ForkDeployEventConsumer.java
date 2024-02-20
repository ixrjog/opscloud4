package com.baiyi.opscloud.event.consumer.impl;

import com.baiyi.opscloud.common.event.NoticeEvent;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.facade.leo.LeoDeployFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/20 17:47
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ForkDeployEventConsumer extends AbstractEventConsumer<LeoDeployParam.DoForkDeploy> {

    private final LeoDeployFacade leoDeployFacade;

    @Override
    public String getEventType() {
        return BusinessTypeEnum.LEO_FORK_DEPLOY.name();
    }

    @Override
    protected void onCreatedMessage(NoticeEvent<LeoDeployParam.DoForkDeploy> noticeEvent) {
        LeoDeployParam.DoForkDeploy doForkDeploy = toEventData(noticeEvent.getMessage());
        log.info("Fork deploy : jobId={}, assetId={}, buildId={}", doForkDeploy.getJobId(), doForkDeploy.getAssetId(), doForkDeploy.getBuildId());
        leoDeployFacade.doForkDeploy(doForkDeploy);
    }

}