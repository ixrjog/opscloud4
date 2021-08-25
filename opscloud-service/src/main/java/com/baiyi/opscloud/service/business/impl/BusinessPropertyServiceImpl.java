package com.baiyi.opscloud.service.business.impl;

import com.baiyi.opscloud.domain.generator.opscloud.BusinessProperty;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.mapper.opscloud.BusinessPropertyMapper;
import com.baiyi.opscloud.service.business.BusinessPropertyService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/21 2:41 下午
 * @Since 1.0
 */

@Service
public class BusinessPropertyServiceImpl implements BusinessPropertyService {

    @Resource
    private BusinessPropertyMapper businessPropertyMapper;

    @Override
    public BusinessProperty getById(int id) {
        return businessPropertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(BusinessProperty businessProperty) {
        businessPropertyMapper.insert(businessProperty);
    }

    @Override
    public void update(BusinessProperty businessProperty) {
        businessPropertyMapper.updateByPrimaryKey(businessProperty);
    }

    @Override
    public void deleteById(int id) {
        businessPropertyMapper.deleteByPrimaryKey(id);
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
    public void deleteByBusinessTypeAndId(Integer businessType, Integer businessId) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType);
        criteria.andEqualTo("businessId", businessId);
        businessPropertyMapper.deleteByExample(example);
    }

}
