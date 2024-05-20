package com.baiyi.opscloud.common.annotation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/17 上午11:45
 * &#064;Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DsTaskLock {

    @Schema(description = "SpEL specified parameters.")
    String instanceId() default "";

    @Schema(description = "Maximum lock time (seconds): Default 60 seconds.")
    String maxLockTime() default "60";

}
