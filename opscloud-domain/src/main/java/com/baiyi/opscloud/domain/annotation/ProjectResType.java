package com.baiyi.opscloud.domain.annotation;

import com.baiyi.opscloud.domain.constants.ProjectResTypeEnum;

import java.lang.annotation.*;

/**
 * @Author 修远
 * @Date 2023/5/19 2:29 PM
 * @Since 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Documented
public @interface ProjectResType {

    ProjectResTypeEnum value();

}