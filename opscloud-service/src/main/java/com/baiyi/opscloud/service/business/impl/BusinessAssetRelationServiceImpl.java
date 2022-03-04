package com.baiyi.opscloud.service.business.impl;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.mapper.opscloud.BusinessAssetRelationMapper;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/30 3:06 下午
 * @Version 1.0
 */
@BusinessType(BusinessTypeEnum.BUSINESS_ASSET_RELATION)
@Service
@RequiredArgsConstructor
public class BusinessAssetRelationServiceImpl implements BusinessAssetRelationService {

    private final BusinessAssetRelationMapper businessAssetRelationMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public BusinessAssetRelation getByUniqueKey(BusinessAssetRelation businessAssetRelation) {
        Example example = new Example(BusinessAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessAssetRelation.getBusinessType())
                .andEqualTo("businessId", businessAssetRelation.getBusinessId())
                .andEqualTo("datasourceInstanceAssetId", businessAssetRelation.getDatasourceInstanceAssetId());
        return businessAssetRelationMapper.selectOneByExample(example);
    }

    @Override
    public void add(BusinessAssetRelation businessAssetRelation) {
        if (businessAssetRelation.getBusinessId() == null || businessAssetRelation.getBusinessId() <= 0) return;
        businessAssetRelationMapper.insert(businessAssetRelation);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.DELETE)
    public void deleteById(Integer id) {
        businessAssetRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<BusinessAssetRelation> queryAssetRelations(int businessType, int datasourceInstanceAssetId) {
        Example example = new Example(BusinessAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("datasourceInstanceAssetId", datasourceInstanceAssetId);
        if (businessType >= 0) criteria.andEqualTo("businessType", businessType);
        return businessAssetRelationMapper.selectByExample(example);
    }

    @Override
    public List<BusinessAssetRelation> queryBusinessRelations(int businessType, int businessId) {
        Example example = new Example(BusinessAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType)
                .andEqualTo("businessId", businessId);
        return businessAssetRelationMapper.selectByExample(example);
    }

    @Override
    public List<BusinessAssetRelation> queryBusinessRelations(BaseBusiness.IBusiness iBusiness, String assetType) {
        Example example = new Example(BusinessAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", iBusiness.getBusinessType())
                .andEqualTo("businessId", iBusiness.getBusinessId())
                .andEqualTo("assetType", assetType);
        return businessAssetRelationMapper.selectByExample(example);
    }

}
