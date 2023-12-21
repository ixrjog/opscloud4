package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2022/11/30 22:03
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RuntimeWrapper {

    boolean extend() default false;

}