package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/6/10 3:42 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SingleTask {

    String name() default "";

    /**
     * second
     *
     * @return
     */
    String lockTime() default "60";

}