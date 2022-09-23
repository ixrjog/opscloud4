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

    private static final long serialVersionUID = 3500050498451084883L;
    /**
     * 接受信息
     */
    @Getter
    private final IEvent message;

    public NoticeEvent(IEvent message) {
        super(message);
        this.message = message;
        log.info("新事件通知: message={}", message);
    }
    
}
