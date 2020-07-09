package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.common.util.IDUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroupProperty;
import com.baiyi.opscloud.mapper.opscloud.OcServerGroupPropertyMapper;
import com.baiyi.opscloud.service.server.OcServerGroupPropertyService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/4 10:30 上午
 * @Version 1.0
 */
@Service
public class OcServerGroupPropertyServiceImpl implements OcServerGroupPropertyService {

    @Resource
    private OcServerGroupPropertyMapper ocServerGroupPropertyMapper;

    @Override
    public OcServerGroupProperty queryOcServerGroupPropertyByUniqueKey(OcServerGroupProperty ocServerGroupProperty) {
        Example example = new Example(OcServerGroupProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", ocServerGroupProperty.getServerGroupId());
        criteria.andEqualTo("envType", ocServerGroupProperty.getEnvType());
        criteria.andEqualTo("propertyName", ocServerGroupProperty.getPropertyName());
        return ocServerGroupPropertyMapper.selectOneByExample(example);
    }

    @Override
    public List<OcServerGroupProperty> queryOcServerGroupPropertyByServerGroupId(int serverGroupId) {
        Example example = new Example(OcServerGroupProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        return ocServerGroupPropertyMapper.selectByExample(example);
    }

    @Override
    public List<OcServerGroupProperty> queryOcServerGroupPropertyByServerGroupIdAndEnvType(int serverGroupId, int envType) {
        Example example = new Example(OcServerGroupProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        criteria.andEqualTo("envType", envType);
        return ocServerGroupPropertyMapper.selectByExample(example);
    }

    @Override
    public List<OcServerGroupProperty> queryOcServerGroupPropertyByServerGroupIdAndEnvTypeAnd(int serverGroupId, int envType, String propertyName) {
        Example example = new Example(OcServerGroupProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        if (!IDUtils.isEmpty(envType))
            criteria.andEqualTo("envType", envType);
        criteria.andEqualTo("propertyName", propertyName);
        return ocServerGroupPropertyMapper.selectByExample(example);
    }

    @Override
    public void addOcServerGroupProperty(OcServerGroupProperty ocServerGroupProperty) {
        ocServerGroupPropertyMapper.insert(ocServerGroupProperty);
    }

    @Override
    public void updateOcServerGroupProperty(OcServerGroupProperty ocServerGroupProperty) {
        ocServerGroupPropertyMapper.updateByPrimaryKey(ocServerGroupProperty);
    }

    @Override
    public void deleteOcServerGroupPropertyById(int id) {
        ocServerGroupPropertyMapper.deleteByPrimaryKey(id);
    }
}
