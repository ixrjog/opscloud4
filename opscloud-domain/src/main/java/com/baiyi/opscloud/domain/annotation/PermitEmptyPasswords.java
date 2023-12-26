package com.baiyi.opscloud.domain.annotation;

import java.lang.annotation.*;

/**
 *
 * 允许空密码登录
 *
 * @Author baiyi
 * @Date 2021/8/4 3:42 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermitEmptyPasswords {
}