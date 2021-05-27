package com.baiyi.caesar.service.tag.impl;

import com.baiyi.caesar.domain.generator.caesar.BusinessTag;
import com.baiyi.caesar.domain.param.tag.BusinessTagParam;
import com.baiyi.caesar.mapper.caesar.BusinessTagMapper;
import com.baiyi.caesar.service.tag.BusinessTagService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/20 11:12 上午
 * @Version 1.0
 */
@Service
public class BusinessTagServiceImpl implements BusinessTagService {

    @Resource
    private BusinessTagMapper businessTagMapper;

    @Override
    public List<BusinessTag> queryByParam(BusinessTagParam.UpdateBusinessTags queryParam) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", queryParam.getBusinessType());
        criteria.andEqualTo("businessId", queryParam.getBusinessId());
        return businessTagMapper.selectByExample(example);
    }

    @Override
    public void add(BusinessTag businessTag) {
        businessTagMapper.insert(businessTag);
    }

    @Override
    public void deleteById(Integer id) {
        businessTagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByBusinessTypeAndId(Integer businessType, Integer businessId) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType);
        criteria.andEqualTo("businessId", businessId);
        businessTagMapper.deleteByExample(example);
    }
}
