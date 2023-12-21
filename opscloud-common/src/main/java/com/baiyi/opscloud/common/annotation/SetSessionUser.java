package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * 设置当前会话用户
 *
 * @Author baiyi
 * @Date 2022/3/7 11:10 AM
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SetSessionUser {

    /**
     * 用户ID
     *
     * @return
     */
    int userId() default 1;

    /**
     * 强制（即使有当前会话有用户标示也强行修改）
     *
     * @return
     */
    boolean force() default true;

}