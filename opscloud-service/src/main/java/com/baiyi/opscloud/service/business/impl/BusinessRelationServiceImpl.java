package com.baiyi.opscloud.service.business.impl;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessRelation;
import com.baiyi.opscloud.mapper.opscloud.BusinessRelationMapper;
import com.baiyi.opscloud.service.business.BusinessRelationService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 2:40 下午
 * @Since 1.0
 */

@Service
public class BusinessRelationServiceImpl implements BusinessRelationService {

    @Resource
    private BusinessRelationMapper businessRelationMapper;

    @Override
    public BusinessRelation getById(int id) {
        return businessRelationMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(BusinessRelation businessRelation) {
        businessRelationMapper.insert(businessRelation);
    }

    @Override
    public void update(BusinessRelation businessProperty) {
        businessRelationMapper.updateByPrimaryKey(businessProperty);
    }

    @Override
    public void deleteById(int id) {
        businessRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<BusinessRelation> listByBusiness(Integer businessType, Integer businessId) {
        Example example = new Example(BusinessRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sourceBusinessType", businessType)
                .andEqualTo("sourceBusinessId", businessId);
        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andEqualTo("targetBusinessType", businessType)
                .andEqualTo("targetBusinessId", businessId);
        example.or(criteria2);
        return businessRelationMapper.selectByExample(example);
    }

    @Override
    public List<BusinessRelation> listBySourceBusiness(Integer businessType, Integer businessId) {
        Example example = new Example(BusinessRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sourceBusinessType", businessType)
                .andEqualTo("sourceBusinessId", businessId);
        return businessRelationMapper.selectByExample(example);
    }

    @Override
    public List<BusinessRelation> listByTargetBusiness(Integer businessType, Integer businessId) {
        Example example = new Example(BusinessRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("targetBusinessType", businessType)
                .andEqualTo("targetBusinessId", businessId);
        return businessRelationMapper.selectByExample(example);
    }

    @Override
    public BusinessRelation getByUnique(BusinessRelation businessRelation) {
        Example example = new Example(BusinessRelation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sourceBusinessType", businessRelation.getSourceBusinessType())
                .andEqualTo("sourceBusinessId", businessRelation.getSourceBusinessId())
                .andEqualTo("targetBusinessType", businessRelation.getTargetBusinessType())
                .andEqualTo("targetBusinessId", businessRelation.getTargetBusinessId());
        return businessRelationMapper.selectOneByExample(example);
    }
}
