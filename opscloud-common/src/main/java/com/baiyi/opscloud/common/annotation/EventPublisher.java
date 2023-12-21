package com.baiyi.opscloud.common.annotation;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;

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

    /**
     * 优先级
     * 1: value
     * 2: @BusinessType注解指定
     * 3: 方法参数 BaseBusiness.IBusiness
     *
     * @return
     */
    BusinessTypeEnum value() default BusinessTypeEnum.COMMON;

    EventActionTypeEnum eventAction();

}