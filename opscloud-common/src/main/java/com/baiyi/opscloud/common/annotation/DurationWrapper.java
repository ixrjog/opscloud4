package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * VO注入持续时间
 *
 * @Author baiyi
 * @Date 2022/2/23 1:09 PM
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DurationWrapper {

    boolean extend() default false;

}