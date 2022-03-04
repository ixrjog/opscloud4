package com.baiyi.opscloud.event.consumer;

import com.baiyi.opscloud.common.event.IEventType;
import com.baiyi.opscloud.common.event.NoticeEvent;

/**
 * @Author baiyi
 * @Date 2021/8/17 6:58 下午
 * @Version 1.0
 */
public interface IEventConsumer extends IEventType {

    void onMessage(NoticeEvent noticeEvent);

}
