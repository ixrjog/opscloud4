package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountGroup;
import com.baiyi.caesar.domain.param.datasource.DsAccountGroupParam;
import com.baiyi.caesar.mapper.caesar.DatasourceAccountGroupMapper;
import com.baiyi.caesar.service.datasource.DsAccountGroupService;
import com.baiyi.caesar.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/15 10:08 上午
 * @Version 1.0
 */
@Service
public class DsAccountGroupServiceImpl implements DsAccountGroupService {

    @Resource
    private DatasourceAccountGroupMapper dsAccountGroupMapper;

    @Override
    public void add(DatasourceAccountGroup datasourceAccountGroup) {
        dsAccountGroupMapper.insert(datasourceAccountGroup);
    }

    @Override
    public void update(DatasourceAccountGroup datasourceAccountGroup) {
        dsAccountGroupMapper.updateByPrimaryKey(datasourceAccountGroup);
    }

    @Override
    public void deleteById(Integer id) {
        dsAccountGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DatasourceAccountGroup getByUniqueKey(String accountUid, String accountId) {
        Example example = new Example(DatasourceAccountGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", accountUid)
                .andEqualTo("accountId", accountId);
        return dsAccountGroupMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<DatasourceAccountGroup> queryPageByParam(DsAccountGroupParam.GroupPageQuery pageQuery){
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(DatasourceAccountGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", pageQuery.getAccountUid());
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
            criteria.andLike("name", likeName);
        }
        if (pageQuery.getIsActive() != null){
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        example.setOrderByClause("create_time");
        List<DatasourceAccountGroup> data = dsAccountGroupMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }
}
