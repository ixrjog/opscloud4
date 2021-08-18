package com.baiyi.opscloud.common.annotation;

import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.EventActionTypeEnum;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/8/17 6:05 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EventPublisher {

    BusinessTypeEnum eventType();

    EventActionTypeEnum eventAction();
}
