package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2023/5/6 18:11
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SetSessionUsername {

    /**
     * 用户名 （SpEL语法）
     *
     * @return
     */
    String usernameSpEL() default "";

    /**
     * 强制（即使有当前会话有用户标示也强行修改）
     * 线程池执行必须重写会话
     *
     * @return
     */
    boolean force() default true;

}