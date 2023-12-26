package com.baiyi.opscloud.domain.annotation;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/8/25 3:27 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BusinessObjectClear {

    BusinessTypeEnum value() default BusinessTypeEnum.COMMON;

}