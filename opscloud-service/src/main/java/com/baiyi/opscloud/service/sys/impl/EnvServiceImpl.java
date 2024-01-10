package com.baiyi.opscloud.service.sys.impl;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.param.sys.EnvParam;
import com.baiyi.opscloud.mapper.EnvMapper;
import com.baiyi.opscloud.service.sys.EnvService;
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
 * @Date 2021/5/25 4:34 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class EnvServiceImpl implements EnvService {

    private final EnvMapper envMapper;

    @Override
    public List<Env> queryAll() {
        return envMapper.selectAll();
    }

    @Override
    public List<Env> queryAllActive() {
        Example example = new Example(Env.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true);
        return envMapper.selectByExample(example);
    }

    @Override
    public void add(Env env) {
        envMapper.insert(env);
    }

    @Override
    public void update(Env env) {
        envMapper.updateByPrimaryKey(env);
    }

    @Override
    public DataTable<Env> queryPageByParam(EnvParam.EnvPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Env.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getEnvName())) {
            criteria.andLike("envName", SQLUtil.toLike(pageQuery.getEnvName()));
        }
        if (IdUtil.isNotEmpty(pageQuery.getEnvType())) {
            criteria.andEqualTo("envType", pageQuery.getEnvType());
        }
        if (pageQuery.getIsActive() != null) {
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        example.setOrderByClause("seq");
        List<Env> data = envMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Env getByEnvType(Integer envType) {
        Example example = new Example(Env.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envType", envType);
        return envMapper.selectOneByExample(example);
    }

    @Override
    public Env getByEnvName(String envName) {
        Example example = new Example(Env.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("envName", envName);
        return envMapper.selectOneByExample(example);
    }

}