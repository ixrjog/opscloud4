package com.baiyi.opscloud.factory.resource.base;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.ProjectResType;
import com.baiyi.opscloud.factory.resource.IProjectResQuery;
import com.baiyi.opscloud.factory.resource.ProjectResQueryFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author 修远
 * @Date 2023/5/19 2:05 PM
 * @Since 1.0
 */
public abstract class AbstractProjectResQuery implements IProjectResQuery, InitializingBean {

    @Override
    public String getProjectResType() {
        if (this.getClass().isAnnotationPresent(ProjectResType.class)) {
            ProjectResType annotation = this.getClass().getAnnotation(ProjectResType.class);
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
        ProjectResQueryFactory.register(this);
    }

}
