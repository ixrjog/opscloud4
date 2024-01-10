package com.baiyi.opscloud.service.business.impl;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.mapper.BusinessPropertyMapper;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author 修远
 * @Date 2021/7/21 2:41 下午
 * @Since 1.0
 */
@Service
@RequiredArgsConstructor
public class BusinessPropertyServiceImpl implements BusinessPropertyService {

    private final BusinessPropertyMapper businessPropertyMapper;

    @Override
    public BusinessProperty getById(int id) {
        return businessPropertyMapper.selectByPrimaryKey(id);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void add(BusinessProperty businessProperty) {
        businessPropertyMapper.insert(businessProperty);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void update(BusinessProperty businessProperty) {
        businessPropertyMapper.updateByPrimaryKey(businessProperty);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void delete(BusinessProperty businessProperty) {
        businessPropertyMapper.deleteByPrimaryKey(businessProperty.getId());
    }

    @Override
    public BusinessProperty getByUniqueKey(Integer businessType, Integer businessId) {
        Example example = new Example(BusinessProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType)
                .andEqualTo("businessId", businessId);
        return businessPropertyMapper.selectOneByExample(example);
    }

    @Override
    public void deleteByUniqueKey(Integer businessType, Integer businessId) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType);
        criteria.andEqualTo("businessId", businessId);
        businessPropertyMapper.deleteByExample(example);
    }

}