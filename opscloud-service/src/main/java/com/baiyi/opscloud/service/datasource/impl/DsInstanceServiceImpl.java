package com.baiyi.opscloud.service.datasource.impl;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.mapper.opscloud.DatasourceInstanceMapper;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/19 6:07 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DsInstanceServiceImpl implements DsInstanceService {

    private final DatasourceInstanceMapper dsInstanceMapper;

    @Override
    public DatasourceInstance getById(Integer id) {
        return dsInstanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public DatasourceInstance getByUuid(String uuid) {
        Example example = new Example(DatasourceInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uuid", uuid);
        return dsInstanceMapper.selectOneByExample(example);
    }

    @Override
    public List<DatasourceInstance> queryByParam(DsInstanceParam.DsInstanceQuery query) {
        Example example = new Example(DatasourceInstance.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(query.getInstanceType())) {
            criteria.andEqualTo("instanceType", query.getInstanceType());
        }
        if (query.getIsActive() != null) {
            criteria.andEqualTo("isActive", query.getIsActive());
        }
        example.setOrderByClause("create_time");
        return dsInstanceMapper.selectByExample(example);
    }

    @Override
    public List<DatasourceInstance> listByInstanceType(String instanceType) {
        Example example = new Example(DatasourceInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceType", instanceType);
        return dsInstanceMapper.selectByExample(example);
    }

    @Override
    public void add(DatasourceInstance datasourceInstance) {
        dsInstanceMapper.insert(datasourceInstance);
    }

    @Override
    public void update(DatasourceInstance datasourceInstance) {
        dsInstanceMapper.updateByPrimaryKey(datasourceInstance);
    }

    @Override
    public int countByConfigId(Integer configId) {
        Example example = new Example(DatasourceInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("configId", configId);
        return dsInstanceMapper.selectCountByExample(example);
    }

}
