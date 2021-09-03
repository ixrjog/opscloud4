package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.mapper.opscloud.InstanceMapper;
import com.baiyi.opscloud.service.sys.InstanceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:26 下午
 * @Version 1.0
 */
@Service
public class InstanceServiceImpl implements InstanceService {

    @Resource
    private InstanceMapper instanceMapper;

    @Override
    public void add(Instance instance) {
        instanceMapper.insert(instance);
    }

    @Override
    public void update(Instance instance) {
        instanceMapper.updateByPrimaryKey(instance);
    }

    @Override
    public void deleteById(int id) {
        instanceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Instance getByHostIp(String hostIp) {
        Example example = new Example(Instance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("hostIp", hostIp);
        return instanceMapper.selectOneByExample(example);
    }

}
