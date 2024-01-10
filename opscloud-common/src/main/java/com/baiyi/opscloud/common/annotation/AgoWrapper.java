package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * VO注入Ago
 * @Author baiyi
 * @Date 2022/2/23 11:07 AM
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AgoWrapper {

    boolean extend() default false;

}