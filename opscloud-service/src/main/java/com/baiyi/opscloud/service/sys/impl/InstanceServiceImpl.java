package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.param.sys.RegisteredInstanceParam;
import com.baiyi.opscloud.mapper.InstanceMapper;
import com.baiyi.opscloud.service.sys.InstanceService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/3 1:26 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class InstanceServiceImpl implements InstanceService {

    private final InstanceMapper instanceMapper;

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
    public Instance getById(Integer id) {
        return instanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public Instance getByHostIp(String hostIp) {
        Example example = new Example(Instance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("hostIp", hostIp);
        return instanceMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<Instance> queryRegisteredInstancePage(RegisteredInstanceParam.RegisteredInstancePageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Instance.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getName())) {
            criteria.andLike("name", SQLUtil.toLike(pageQuery.getName()));
        }
        if (pageQuery.getIsActive() != null) {
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        example.setOrderByClause("name");
        List<Instance> data = instanceMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<Instance> listActiveInstance() {
        Example example = new Example(Instance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true);
        return instanceMapper.selectByExample(example);
    }

}