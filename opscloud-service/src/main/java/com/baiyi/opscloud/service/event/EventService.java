package com.baiyi.opscloud.service.event;

import com.baiyi.opscloud.domain.generator.opscloud.Event;

/**
 * @Author baiyi
 * @Date 2021/10/9 2:26 下午
 * @Version 1.0
 */
public interface EventService {

    void add(Event event);

    void update(Event event);

}
