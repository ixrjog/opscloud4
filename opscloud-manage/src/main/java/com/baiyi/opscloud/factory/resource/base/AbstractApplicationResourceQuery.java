package com.baiyi.opscloud.factory.resource.base;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.domain.annotation.ApplicationResType;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.factory.resource.ApplicationResourceQueryFactory;
import com.baiyi.opscloud.factory.resource.IApplicationResourceQuery;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2021/9/8 4:31 下午
 * @Version 1.0
 */
public abstract class AbstractApplicationResourceQuery implements IApplicationResourceQuery, InitializingBean {

   //  protected abstract DataTable<ApplicationResourceVO.Resource> queryResourcePage(ApplicationResourceParam.ResourcePageQuery pageQuery);

    @Override
    public String getApplicationResType() {
        if (this.getClass().isAnnotationPresent(ApplicationResType.class)) {
            ApplicationResType annotation = this.getClass().getAnnotation(ApplicationResType.class);
            return annotation.value().name();
        }
        throw new CommonRuntimeException("未找到@ApplicationResourceType注解,无法指定应用资源类型");
    }

    @Override
    public Integer getBusinessType() {
        if (this.getClass().isAnnotationPresent(BusinessType.class)) {
            BusinessType annotation = this.getClass().getAnnotation(BusinessType.class);
            return annotation.value().getType();
        }
        throw new CommonRuntimeException("未找到@BusinessType注解,无法指定业务类型");
    }

    @Override
    public void afterPropertiesSet() {
        ApplicationResourceQueryFactory.register(this);
    }

}
