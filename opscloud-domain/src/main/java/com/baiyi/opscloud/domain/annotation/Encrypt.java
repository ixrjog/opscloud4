package com.baiyi.opscloud.domain.annotation;

import java.lang.annotation.*;

/**
 * @Author 修远
 * @Date 2021/6/11 10:39 上午
 * @Since 1.0
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {

}