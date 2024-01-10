package com.baiyi.opscloud.domain.annotation;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 * 解除用户授权
 * @Author baiyi
 * @Date 2021/8/18 1:59 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public  @interface RevokeUserPermission {

    BusinessTypeEnum value() default BusinessTypeEnum.COMMON;

}