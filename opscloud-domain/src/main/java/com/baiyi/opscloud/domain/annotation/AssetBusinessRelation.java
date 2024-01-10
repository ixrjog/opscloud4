package com.baiyi.opscloud.domain.annotation;

import java.lang.annotation.*;

/**
 * 绑定资产与业务对象
 *
 * @Author baiyi
 * @Date 2021/8/6 5:29 下午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AssetBusinessRelation {

}