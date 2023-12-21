package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * @Author 修远
 * @Date 2022/7/25 3:45 PM
 * @Since 1.0
 */


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ServiceExceptionCatch {

    String message() default "Primary Key conflict";

}