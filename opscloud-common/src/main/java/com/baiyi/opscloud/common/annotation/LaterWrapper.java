package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2022/2/23 11:33 AM
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LaterWrapper {

    boolean extend() default false;

}