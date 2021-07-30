package com.baiyi.opscloud.service.business;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessRelation;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 2:40 下午
 * @Since 1.0
 */
@Deprecated
public interface BusinessRelationService {

    BusinessRelation getById(int id);

    void add(BusinessRelation businessRelation);

    void update(BusinessRelation businessProperty);

    void deleteById(int id);

    List<BusinessRelation> listByBusiness(Integer businessType, Integer businessId);

    List<BusinessRelation> listBySourceBusiness(Integer businessType, Integer businessId);

    List<BusinessRelation> listByTargetBusiness(Integer businessType, Integer businessId);

    BusinessRelation getByUniqueKey(BusinessRelation businessRelation);

    BusinessRelation getBusinessRelation(Integer sourceBusinessType, Integer sourceBusinessId, Integer targetBusinessType, String relationType);
}
