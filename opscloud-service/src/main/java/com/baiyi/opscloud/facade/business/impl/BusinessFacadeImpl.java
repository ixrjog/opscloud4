package com.baiyi.opscloud.facade.business.impl;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessRelation;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.facade.business.BusinessFacade;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import com.baiyi.opscloud.service.business.BusinessRelationService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/22 3:53 下午
 * @Since 1.0
 */

@Service
public class BusinessFacadeImpl implements BusinessFacade {

    @Resource
    private BusinessPropertyService businessPropertyService;

    @Resource
    private BusinessRelationService businessRelationService;

    @Override
    public Map<String, String> getBusinessProperty(Integer businessType, Integer businessId) {
        List<BusinessProperty> properties = businessPropertyService.listByBusiness(businessType, businessId);
        Map<String, String> map = Maps.newHashMapWithExpectedSize(properties.size());
        properties.forEach(property -> map.put(property.getName(), property.getValue()));
        return map;
    }

    @Override
    public void saveBusinessRelation(BusinessRelation businessRelation) {
        if (businessRelationService.getByUnique(businessRelation) == null) {
            businessRelationService.add(businessRelation);
        }
    }

    @Override
    public void deleteBusinessRelation(Integer businessType, Integer businessId) {
        List<BusinessRelation> businessRelations = businessRelationService.listByBusiness(businessType, businessId);
        if (!CollectionUtils.isEmpty(businessRelations)) {
            businessRelations.forEach(relation -> businessRelationService.deleteById(relation.getId()));
        }
    }

    @Override
    public Map<String, String> getDefaultServerGroupProperty() {
        return getBusinessProperty(BusinessTypeEnum.SERVERGROUP.getType(), 0);
    }

    @Override
    public BusinessRelation getBusinessRelation(Integer sourceBusinessType, Integer sourceBusinessId, Integer targetBusinessType, String relationType) {
        return businessRelationService.getBusinessRelation(sourceBusinessType, sourceBusinessId,targetBusinessType, relationType);
    }
}
