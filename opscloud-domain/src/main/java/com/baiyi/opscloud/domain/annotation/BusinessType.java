package com.baiyi.opscloud.domain.annotation;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 * 业务类型
 * @Author baiyi
 * @Date 2021/8/25 3:37 下午
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Documented
public @interface BusinessType {

    BusinessTypeEnum value();

}