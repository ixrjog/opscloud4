package com.baiyi.opscloud.domain.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2021/6/16 10:37 上午
 * @Version 1.0
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decrypt {

}