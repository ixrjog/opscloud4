package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessDocument;

/**
 * @Author baiyi
 * @Date 2022/5/15 19:05
 * @Version 1.0
 */
public interface BusinessDocumentService {

    void add(BusinessDocument businessDocument);

    void update(BusinessDocument businessDocument);

    BusinessDocument getById(int id);

    BusinessDocument getByBusiness(BaseBusiness.IBusiness iBusiness);

    void deleteByUniqueKey(Integer businessType, Integer businessId);

}