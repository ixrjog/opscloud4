package com.baiyi.opscloud.common.annotation;

import java.lang.annotation.*;

/**
 * @Author God
 * @Date 2023/2/21 13:35
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ArkIntercept {

    boolean bigFlood() default true;

    String doomsday() default "2099-08-16 12:30:55";

}