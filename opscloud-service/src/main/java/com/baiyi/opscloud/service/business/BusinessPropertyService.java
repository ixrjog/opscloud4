package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.base.BaseBusiness;

/**
 * @Author 修远
 * @Date 2021/7/21 2:40 下午
 * @Since 1.0
 */
public interface BusinessPropertyService {

    BusinessProperty getById(int id);

    void add(BusinessProperty businessProperty);

    void update(BusinessProperty businessProperty);

    void delete(BusinessProperty businessProperty);

    BusinessProperty getByUniqueKey(Integer businessType, Integer businessId);

    default BusinessProperty getByUniqueKey(BaseBusiness.IBusiness iBusiness) {
        return getByUniqueKey(iBusiness.getBusinessType(), iBusiness.getBusinessId());
    }

    void deleteByUniqueKey(Integer businessType, Integer businessId);

}