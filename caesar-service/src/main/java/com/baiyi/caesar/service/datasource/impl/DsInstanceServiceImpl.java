package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.param.datasource.DsInstanceParam;
import com.baiyi.caesar.mapper.caesar.DatasourceInstanceMapper;
import com.baiyi.caesar.service.datasource.DsInstanceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/19 6:07 下午
 * @Version 1.0
 */
@Service
public class DsInstanceServiceImpl implements DsInstanceService {

    @Resource
    private DatasourceInstanceMapper dsInstanceMapper;

    @Override
    public DatasourceInstance getById(Integer id){
        return dsInstanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DatasourceInstance> queryByParam(DsInstanceParam.DsInstanceQuery query) {
        Example example = new Example(DatasourceInstance.class);
        Example.Criteria criteria = example.createCriteria();
        if (IdUtil.isNotEmpty(query.getInstanceType())) {
            criteria.andEqualTo("instanceType", query.getInstanceType());
        }
        if (query.getIsActive() != null) {
            criteria.andEqualTo("isActive", query.getIsActive());
        }
        example.setOrderByClause("create_time");
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
    public int countByConfigId(Integer configId){
        Example example = new Example(DatasourceInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("configId", configId);
        return dsInstanceMapper.selectCountByExample(example);
    }

}
