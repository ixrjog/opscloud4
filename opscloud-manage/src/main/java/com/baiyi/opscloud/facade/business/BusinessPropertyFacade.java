package com.baiyi.opscloud.facade.business;

import com.baiyi.opscloud.domain.param.server.business.BusinessPropertyParam;

/**
 * @Author baiyi
 * @Date 2021/8/19 4:40 下午
 * @Version 1.0
 */
public interface BusinessPropertyFacade {

    void add(BusinessPropertyParam.Property property);

    void update(BusinessPropertyParam.Property property);

}
