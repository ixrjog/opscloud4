package com.baiyi.opscloud.leo.handler.deploy.chain.post.event;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/8/30 20:36
 * @Version 1.0
 */
@Component
public class AliyunEventBridgeDeployEventPublisher {

    @EventPublisher(value = BusinessTypeEnum.EVENT_BRIDGE_DEPLOY_EVENT, eventAction = EventActionTypeEnum.CREATE)
    public void publish(LeoHook.DeployHook hook) {
        // 父模块处理事件
    }

}