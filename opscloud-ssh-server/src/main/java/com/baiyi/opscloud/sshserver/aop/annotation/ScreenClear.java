package com.baiyi.opscloud.sshserver.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/7/2 6:22 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ScreenClear  {

    String value() default "";

}