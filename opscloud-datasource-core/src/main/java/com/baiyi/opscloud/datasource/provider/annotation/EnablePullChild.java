package com.baiyi.opscloud.datasource.provider.annotation;

import com.baiyi.opscloud.common.type.DsAssetTypeEnum;

import java.lang.annotation.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/6 5:39 下午
 * @Since 1.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnablePullChild {

    DsAssetTypeEnum type();
}
