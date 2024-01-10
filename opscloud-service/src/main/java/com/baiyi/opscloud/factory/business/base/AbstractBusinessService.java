package com.baiyi.opscloud.factory.business.base;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.factory.business.BusinessServiceFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 抽象业务Service
 * @Author baiyi
 * @Date 2021/9/8 10:09 上午
 * @Version 1.0
 */
public abstract class AbstractBusinessService<T> implements IBusinessService<T>, InitializingBean {

    @Override
    public Integer getBusinessType() {
        if (this.getClass().isAnnotationPresent(BusinessType.class)) {
            BusinessType annotation = this.getClass().getAnnotation(BusinessType.class);
            return annotation.value().getType();
        }
        throw new OCException("未找到注解`@BusinessType`, 无法指定业务类型!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BusinessServiceFactory.register(this);
    }

}