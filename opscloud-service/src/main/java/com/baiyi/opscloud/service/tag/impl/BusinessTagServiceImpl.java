package com.baiyi.opscloud.service.tag.impl;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTag;
import com.baiyi.opscloud.mapper.BusinessTagMapper;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/20 11:12 上午
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class BusinessTagServiceImpl implements BusinessTagService {

    private final BusinessTagMapper businessTagMapper;

    @Override
    public List<BusinessTag> queryByBusiness(BaseBusiness.IBusiness iBusiness) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(BaseBusiness.BUSINESS_TYPE, iBusiness.getBusinessType());
        criteria.andEqualTo(BaseBusiness.BUSINESS_ID, iBusiness.getBusinessId());
        return businessTagMapper.selectByExample(example);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
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
        criteria.andEqualTo(BaseBusiness.BUSINESS_TYPE, businessType);
        criteria.andEqualTo(BaseBusiness.BUSINESS_ID, businessId);
        businessTagMapper.deleteByExample(example);
    }

    @Override
    public int countByBusinessTag(BusinessTag businessTag) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(BaseBusiness.BUSINESS_TYPE, businessTag.getBusinessType())
                .andEqualTo(BaseBusiness.BUSINESS_ID, businessTag.getBusinessId())
                .andEqualTo("tagId", businessTag.getTagId());
        return businessTagMapper.selectCountByExample(example);
    }

    @Override
    public int countByTagId(Integer tagId) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tagId", tagId);
        return businessTagMapper.selectCountByExample(example);
    }

}