package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2022/2/9 10:54 AM
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TagsWrapper {

    boolean extend() default false;

    boolean wrapResult() default false;

}