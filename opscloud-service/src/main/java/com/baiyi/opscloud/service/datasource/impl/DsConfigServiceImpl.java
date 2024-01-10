package com.baiyi.opscloud.service.datasource.impl;

import com.baiyi.opscloud.common.annotation.ServiceExceptionCatch;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.param.datasource.DsConfigParam;
import com.baiyi.opscloud.factory.credential.AbstractCredentialCustomer;
import com.baiyi.opscloud.mapper.DatasourceConfigMapper;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:45 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class DsConfigServiceImpl extends AbstractCredentialCustomer implements DsConfigService {

    private final DatasourceConfigMapper dsConfigMapper;

    @Override
    public String getBeanName() {
        return "DsConfigService";
    }

    @Override
    public DatasourceConfig getById(Integer id) {
        return dsConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    @ServiceExceptionCatch(message = "新增数据源配置错误: 请确认名称字段是否唯一!")
    public void add(DatasourceConfig datasourceConfig) {
        dsConfigMapper.insert(datasourceConfig);
    }

    @Override
    @ServiceExceptionCatch(message = "更新数据源配置错误: 请确认名称字段是否唯一!")
    public void update(DatasourceConfig datasourceConfig) {
        dsConfigMapper.updateByPrimaryKey(datasourceConfig);
    }

    @Override
    public DataTable<DatasourceConfig> queryPageByParam(DsConfigParam.DsConfigPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<DatasourceConfig> data = dsConfigMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<DatasourceConfig> queryByDsType(Integer dsType) {
        Example example = new Example(DatasourceConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dsType", dsType)
                .andEqualTo("isActive", true);
        example.setOrderByClause("create_time");
        return dsConfigMapper.selectByExample(example);
    }

    @Override
    public int countByCredentialId(int credentialId) {
        Example example = new Example(DatasourceConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("credentialId", credentialId);
        return dsConfigMapper.selectCountByExample(example);
    }

}