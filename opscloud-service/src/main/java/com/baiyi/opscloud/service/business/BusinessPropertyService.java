package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 2:40 下午
 * @Since 1.0
 */
public interface BusinessPropertyService {

    BusinessProperty getById(int id);

    void add(BusinessProperty businessProperty);

    void update(BusinessProperty businessProperty);

    void deleteById(int id);

    BusinessProperty getByUniqueKey(Integer businessType, Integer businessId);

    default BusinessProperty getByUniqueKey(BaseBusiness.IBusiness iBusiness) {
        return getByUniqueKey(iBusiness.getBusinessType(), iBusiness.getBusinessId());
    }

    void deleteByBusinessTypeAndId(Integer businessType, Integer businessId);
}
