package com.baiyi.opscloud.domain.annotation;

import java.lang.annotation.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/11 10:39 上午
 * @Since 1.0
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {

}
