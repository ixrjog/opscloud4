package com.baiyi.opscloud.leo.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2023/1/6 14:04
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LeoBuildInterceptor {

    boolean OFF = false;

    boolean ON = true;

    /**
     * 任务ID （SpEL语法）
     *
     * @return
     */
    String jobIdSpEL() default "";

    /**
     * 任务锁
     *
     * @return
     */
    boolean lock() default OFF;

}