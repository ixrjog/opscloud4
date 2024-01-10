package com.baiyi.opscloud.domain.annotation;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;

import java.lang.annotation.*;

/**
 * 资产类型
 *
 * @Author baiyi
 * @Date 2021/9/8 3:12 下午
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Documented
public @interface AssetType {

    DsAssetTypeConstants value();

}