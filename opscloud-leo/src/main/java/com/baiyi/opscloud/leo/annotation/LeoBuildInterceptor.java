package com.baiyi.opscloud.leo.annotation;

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

    public static final boolean DISABLED = false;

    /**
     * 任务ID （SpEL语法）
     * @return
     */
    String jobIdSpEL() default "";

    /**
     * 允许并发
     * @return
     */
    boolean allowConcurrency() default true;

}
