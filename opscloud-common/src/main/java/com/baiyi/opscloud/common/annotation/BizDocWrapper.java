package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * VO注入业务文档
 * @Author baiyi
 * @Date 2022/5/15 19:26
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BizDocWrapper {

    boolean extend() default false;

    boolean wrapResult() default false;

}