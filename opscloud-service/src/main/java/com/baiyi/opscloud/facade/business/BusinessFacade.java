package com.baiyi.opscloud.facade.business;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessRelation;

import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 3:53 下午
 * @Since 1.0
 */
public interface BusinessFacade {

    Map<String, String> getBusinessProperty(Integer businessType, Integer businessId);

    void saveBusinessRelation(BusinessRelation businessRelation);

    void deleteBusinessRelation(Integer businessType, Integer businessId);

    Map<String, String> getDefaultServerGroupProperty();

    BusinessRelation getBusinessRelation(Integer sourceBusinessType, Integer sourceBusinessId,Integer targetBusinessType, String relationType);

}
