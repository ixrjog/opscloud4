package com.baiyi.opscloud.leo.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2022/12/29 13:22
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LeoDeployInterceptor {

    boolean OFF = false;

    boolean ON = true;

    /**
     * 任务ID （SpEL语法）
     *
     * @return
     */
    String jobIdSpEL() default "";


    String deployTypeSpEL() default "";

    String buildIdSpEL() default "";

    String deploymentAssetIdSpEL() default "";

    /**
     * 任务锁
     *
     * @return
     */
    boolean lock() default ON;

}