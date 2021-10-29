package com.baiyi.opscloud.facade.event;

import com.baiyi.opscloud.domain.generator.opscloud.Event;

/**
 * @Author baiyi
 * @Date 2021/10/9 2:57 下午
 * @Version 1.0
 */
public interface EventFacade {

    void recordEvent(Event event);

}
