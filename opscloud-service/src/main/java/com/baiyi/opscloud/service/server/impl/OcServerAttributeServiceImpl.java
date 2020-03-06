package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.generator.OcServerAttribute;
import com.baiyi.opscloud.mapper.OcServerAttributeMapper;
import com.baiyi.opscloud.service.server.OcServerAttributeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/6 4:41 下午
 * @Version 1.0
 */
@Service
public class OcServerAttributeServiceImpl implements OcServerAttributeService {
    @Resource
    private OcServerAttributeMapper ocServerAttributeMapper;

    @Override
    public OcServerAttribute queryOcServerAttributeByUniqueKey(OcServerAttribute ocServerAttribute) {
        Example example = new Example(OcServerAttribute.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessId", ocServerAttribute.getBusinessId());
        criteria.andEqualTo("businessType", ocServerAttribute.getBusinessType());
        criteria.andEqualTo("groupName", ocServerAttribute.getGroupName());
        return ocServerAttributeMapper.selectOneByExample(example);
    }
}
