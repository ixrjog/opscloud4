package com.baiyi.opscloud.common.event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * @Author baiyi
 * @Date 2021/8/17 4:26 下午
 * @Version 1.0
 */
@Slf4j
public class NoticeEvent extends ApplicationEvent {

    /**
     * 接受信息
     */
    @Getter
    private final IEvent message;

    public NoticeEvent(IEvent message) {
        super(message);
        this.message = message;
        log.info("新事件 Event success! message: {}", message);
    }


}
