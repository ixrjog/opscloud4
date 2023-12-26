package com.baiyi.opscloud.domain.annotation;

import java.lang.annotation.*;

/**
 * @Author 修远
 * @Date 2021/6/11 11:35 上午
 * @Since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DesensitizedMethod {

}