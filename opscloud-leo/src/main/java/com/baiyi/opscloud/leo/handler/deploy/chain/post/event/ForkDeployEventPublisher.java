package com.baiyi.opscloud.leo.handler.deploy.chain.post.event;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/21 16:48
 * @Version 1.0
 */
@Component
public class ForkDeployEventPublisher {

    @EventPublisher(value = BusinessTypeEnum.LEO_FORK_DEPLOY, eventAction = EventActionTypeEnum.CREATE)
    public void publish(LeoDeployParam.DoForkDeploy doForkDeploy) {
    }

}
