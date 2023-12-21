package com.baiyi.opscloud.common.event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2021/8/17 4:26 下午
 * @Version 1.0
 */
@Getter
@Slf4j
public class NoticeEvent<T> extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 3500050498451084883L;
    /**
     * 接受信息
     */
    private final IEvent<T> message;

    public NoticeEvent(IEvent<T> message) {
        super(message);
        this.message = message;
        log.info("新事件通知: message={}", message);
    }
    
}