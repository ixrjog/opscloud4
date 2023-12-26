package com.baiyi.opscloud.core.provider.annotation;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;

import java.lang.annotation.*;

/**
 * @Author 修远
 * @Date 2021/7/6 5:39 下午
 * @Since 1.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnablePullChild {

    DsAssetTypeConstants type();

}