package com.baiyi.opscloud.domain.annotation;

import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;

import java.lang.annotation.*;

/**
 * 应用资源类型注解
 *
 * @Author baiyi
 * @Date 2021/9/8 4:14 下午
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Documented
public @interface ApplicationResType {

    ApplicationResTypeEnum value();

}