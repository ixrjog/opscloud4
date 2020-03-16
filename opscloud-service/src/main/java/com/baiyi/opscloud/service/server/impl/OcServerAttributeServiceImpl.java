package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerAttribute;
import com.baiyi.opscloud.mapper.opscloud.OcServerAttributeMapper;
import com.baiyi.opscloud.service.server.OcServerAttributeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<OcServerAttribute> queryOcServerAttributeByBusinessTypeAndBusinessId(int businessType, int businessId) {
        Example example = new Example(OcServerAttribute.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessId", businessId);
        criteria.andEqualTo("businessType", businessType);
        return ocServerAttributeMapper.selectByExample(example);
    }

    @Override
    public void addOcServerAttribute(OcServerAttribute ocServerAttribute) {
        ocServerAttributeMapper.insert(ocServerAttribute);
    }

    @Override
    public void updateOcServerAttribute(OcServerAttribute ocServerAttribute) {
        ocServerAttributeMapper.updateByPrimaryKey(ocServerAttribute);
    }

    @Override
    public void deleteOcServerAttributeById(int id) {
        ocServerAttributeMapper.deleteByPrimaryKey(id);
    }
}
