package com.baiyi.caesar.service.tag;

import com.baiyi.caesar.domain.generator.caesar.BusinessTag;
import com.baiyi.caesar.domain.param.tag.BusinessTagParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/20 11:12 上午
 * @Version 1.0
 */
public interface BusinessTagService {

    List<BusinessTag> queryByParam(BusinessTagParam.UpdateBusinessTags queryParam);

    void add(BusinessTag businessTag);

    void deleteById(Integer id);

    void deleteByBusinessTypeAndId(Integer businessType, Integer businessId);

    int countByParam(BusinessTag businessTag);
}
