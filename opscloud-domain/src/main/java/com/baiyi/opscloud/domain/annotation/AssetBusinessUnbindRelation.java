package com.baiyi.opscloud.domain.annotation;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 *  解除资产与业务对象绑定关系
 * @Author baiyi
 * @Date 2021/8/10 11:39 上午
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AssetBusinessUnbindRelation {

    BusinessTypeEnum type();

}