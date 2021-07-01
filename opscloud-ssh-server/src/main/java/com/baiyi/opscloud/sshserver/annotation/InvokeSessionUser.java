package com.baiyi.opscloud.sshserver.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/6/28 2:53 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface InvokeSessionUser {

    String name() default "";

    boolean invokeAdmin() default false;
}
