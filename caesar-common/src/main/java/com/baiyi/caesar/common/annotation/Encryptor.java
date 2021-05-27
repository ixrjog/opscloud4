package com.baiyi.caesar.common.annotation;

/**
 * @Author baiyi
 * @Date 2021/5/18 10:24 上午
 * @Version 1.0
 */

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Encryptor {

    String value() default "";

}
