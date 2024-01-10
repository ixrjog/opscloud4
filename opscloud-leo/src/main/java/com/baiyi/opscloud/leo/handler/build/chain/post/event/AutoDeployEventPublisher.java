package com.baiyi.opscloud.leo.handler.build.chain.post.event;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/8 14:14
 * @Version 1.0
 */
@Component
public class AutoDeployEventPublisher {

    @EventPublisher(value = BusinessTypeEnum.LEO_AUTO_DEPLOY, eventAction = EventActionTypeEnum.CREATE)
    public void publish(LeoDeployParam.DoAutoDeploy doAutoDeploy) {
        // 父模块处理事件
    }

}