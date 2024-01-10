package com.baiyi.opscloud.sshserver.aop.annotation;

import com.baiyi.opscloud.common.constants.enums.SessionSource;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/6/28 2:53 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SettingContextSessionUser {

    String name() default "";

    SessionSource source() default SessionSource.SSH_SHELL;

    boolean invokeAdmin() default false;

}