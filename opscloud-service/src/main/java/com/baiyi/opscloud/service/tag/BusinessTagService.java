package com.baiyi.opscloud.service.tag;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/20 11:12 上午
 * @Version 1.0
 */
public interface BusinessTagService {

    List<BusinessTag> queryByBusiness(BaseBusiness.IBusiness iBusiness);

    void add(BusinessTag businessTag);

    void deleteById(Integer id);

    void deleteByBusinessTypeAndId(Integer businessType, Integer businessId);

    int countByBusinessTag(BusinessTag businessTag);

    int countByTagId(Integer tagId);

}