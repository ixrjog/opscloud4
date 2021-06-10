package com.baiyi.caesar.common.annotation;

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

    int lockSecond() default 60;
}
