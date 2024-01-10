package com.baiyi.opscloud.leo.handler.build.chain.post.event;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.hook.leo.LeoHook;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/15 15:25
 * @Version 1.0
 */
@Component
public class MeterSphereBuildEventPublisher {

    @EventPublisher(value = BusinessTypeEnum.METER_SPHERE_BUILD_HOOK, eventAction = EventActionTypeEnum.CREATE)
    public void publish(LeoHook.BuildHook hook) {
        // 父模块处理事件
    }

}