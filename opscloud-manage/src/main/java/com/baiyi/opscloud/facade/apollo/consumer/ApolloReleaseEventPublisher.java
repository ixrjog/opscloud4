package com.baiyi.opscloud.facade.apollo.consumer;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.core.entity.InterceptRelease;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/4/9 下午2:16
 * @Version 1.0
 */
@Component
public class ApolloReleaseEventPublisher {

    @EventPublisher(value = BusinessTypeEnum.EVENT_BRIDGE_APOLLO_RELEASE_EVENT, eventAction = EventActionTypeEnum.CREATE)
    public void publish(InterceptRelease.Event event) {
        // 父模块处理事件
    }

}
