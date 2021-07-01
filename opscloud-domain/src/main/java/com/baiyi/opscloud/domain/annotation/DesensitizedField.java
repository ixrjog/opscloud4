package com.baiyi.opscloud.domain.annotation;

import com.baiyi.opscloud.domain.types.SensitiveTypeEnum;

import java.lang.annotation.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/11 10:53 上午
 * @Since 1.0
 */


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DesensitizedField {

    SensitiveTypeEnum type();
}

