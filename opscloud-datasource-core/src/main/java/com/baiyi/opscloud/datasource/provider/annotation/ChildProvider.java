package com.baiyi.opscloud.datasource.provider.annotation;

import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;

import java.lang.annotation.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/6 5:26 下午
 * @Since 1.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ChildProvider {

    DsAssetTypeEnum parentType();
}
