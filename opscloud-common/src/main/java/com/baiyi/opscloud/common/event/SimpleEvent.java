package com.baiyi.opscloud.common.event;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/8/17 4:37 下午
 * @Version 1.0
 */
@Builder
@Data
public class SimpleEvent<T> implements IEvent<T> {

    private String eventType;

    private String action;

    private T body;

}