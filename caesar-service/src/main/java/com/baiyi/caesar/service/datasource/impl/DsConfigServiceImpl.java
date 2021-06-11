package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.param.datasource.DsConfigParam;
import com.baiyi.caesar.mapper.caesar.DatasourceConfigMapper;
import com.baiyi.caesar.service.datasource.DsConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:45 下午
 * @Version 1.0
 */
@Service
public class DsConfigServiceImpl implements DsConfigService {

    @Resource
    private DatasourceConfigMapper dsConfigMapper;

    @Override
    public DatasourceConfig getById(Integer id) {
        return dsConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(DatasourceConfig datasourceConfig) {
        dsConfigMapper.insert(datasourceConfig);
    }

    @Override
    public void update(DatasourceConfig datasourceConfig) {
        dsConfigMapper.updateByPrimaryKey(datasourceConfig);
    }

    @Override
    public DataTable<DatasourceConfig> queryPageByParam(DsConfigParam.DsConfigPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<DatasourceConfig> data = dsConfigMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<DatasourceConfig> queryByDsType(Integer dsType) {
        Example example = new Example(DatasourceConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dsType", dsType).andEqualTo("isActive", true);
        example.setOrderByClause("create_time");
        return dsConfigMapper.selectByExample(example);
    }

}
