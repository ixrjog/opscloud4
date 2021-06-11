package com.baiyi.caesar.domain.annotation;

import java.lang.annotation.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/11 11:35 上午
 * @Since 1.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DesensitizedMethod {
}
