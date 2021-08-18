package com.baiyi.opscloud.event.customer;

import com.baiyi.opscloud.event.IEventType;
import com.baiyi.opscloud.event.NoticeEvent;

/**
 * @Author baiyi
 * @Date 2021/8/17 6:58 下午
 * @Version 1.0
 */
public interface IEventConsumer extends IEventType {

    void onMessage(NoticeEvent noticeEvent);

}
