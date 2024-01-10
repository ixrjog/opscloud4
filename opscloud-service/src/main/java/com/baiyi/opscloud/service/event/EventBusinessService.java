package com.baiyi.opscloud.service.event;

import com.baiyi.opscloud.domain.generator.opscloud.EventBusiness;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/11 5:54 下午
 * @Version 1.0
 */
public interface EventBusinessService {

    void add(EventBusiness eventBusiness);

    List<EventBusiness> queryByEventId(Integer eventId);

    EventBusiness getById(Integer id);

    void deleteById(Integer id);

}