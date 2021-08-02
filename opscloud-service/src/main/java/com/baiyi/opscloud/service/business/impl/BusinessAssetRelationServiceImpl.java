package com.baiyi.opscloud.service.business.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessAssetRelation;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.mapper.opscloud.BusinessAssetRelationMapper;
import com.baiyi.opscloud.service.business.BusinessAssetRelationService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/30 3:06 下午
 * @Version 1.0
 */
@Service
public class BusinessAssetRelationServiceImpl implements BusinessAssetRelationService {

    @Resource
    private BusinessAssetRelationMapper businessAssetRelationMapper;

    @Override
    public BusinessAssetRelation getByUniquekey(BusinessAssetRelation businessAssetRelation) {
        Example example = new Example(BusinessAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessAssetRelation.getBusinessType())
                .andEqualTo("businessId", businessAssetRelation.getBusinessId())
                .andEqualTo("datasourceInstanceAssetId", businessAssetRelation.getDatasourceInstanceAssetId());
        return businessAssetRelationMapper.selectOneByExample(example);
    }

    @Override
    public BusinessAssetRelation getByUniquekey(BusinessAssetRelationVO.Relation relation) {
        return getByUniquekey(BeanCopierUtil.copyProperties(relation, BusinessAssetRelation.class));
    }

    @Override
    public void add(BusinessAssetRelation businessAssetRelation) {
        businessAssetRelationMapper.insert(businessAssetRelation);
    }

    @Override
    public List<BusinessAssetRelation> queryAssetRelations(int businessType, int datasourceInstanceAssetId) {
        Example example = new Example(BusinessAssetRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType)
                .andEqualTo("datasourceInstanceAssetId", datasourceInstanceAssetId);
        return businessAssetRelationMapper.selectByExample(example);
    }

}
