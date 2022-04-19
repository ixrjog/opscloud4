package com.baiyi.opscloud.facade.business;

import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;

/**
 * @Author baiyi
 * @Date 2021/8/19 4:40 下午
 * @Version 1.0
 */
public interface BusinessPropertyFacade {

    void add(BusinessPropertyVO.Property property);

    void update(BusinessPropertyVO.Property property);

}
