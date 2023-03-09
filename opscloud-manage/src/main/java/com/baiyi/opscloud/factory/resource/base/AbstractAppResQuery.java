package com.baiyi.opscloud.factory.resource.base;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.domain.annotation.ApplicationResType;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.factory.resource.AppResQueryFactory;
import com.baiyi.opscloud.factory.resource.IAppResQuery;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2021/9/8 4:31 下午
 * @Version 1.0
 */
public abstract class AbstractAppResQuery implements IAppResQuery, InitializingBean {

    @Override
    public String getAppResType() {
        if (this.getClass().isAnnotationPresent(ApplicationResType.class)) {
            ApplicationResType annotation = this.getClass().getAnnotation(ApplicationResType.class);
            return annotation.value().name();
        }
        throw new OCException("未找到@ApplicationResourceType注解,无法指定应用资源类型");
    }

    @Override
    public Integer getBusinessType() {
        if (this.getClass().isAnnotationPresent(BusinessType.class)) {
            BusinessType annotation = this.getClass().getAnnotation(BusinessType.class);
            return annotation.value().getType();
        }
        throw new OCException("未找到@BusinessType注解,无法指定业务类型");
    }

    @Override
    public void afterPropertiesSet() {
        AppResQueryFactory.register(this);
    }

}
