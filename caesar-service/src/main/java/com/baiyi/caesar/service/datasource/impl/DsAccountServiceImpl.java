package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.param.datasource.DsAccountParam;
import com.baiyi.caesar.mapper.caesar.DatasourceAccountMapper;
import com.baiyi.caesar.service.datasource.DsAccountService;
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
 * @Date 2021/6/10 10:09 上午
 * @Version 1.0
 */
@Service
public class DsAccountServiceImpl implements DsAccountService {

    @Resource
    private DatasourceAccountMapper dsAccountMapper;

    @Override
    public DataTable<DatasourceAccount> queryPageByParam(DsAccountParam.AccountPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(DatasourceAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", pageQuery.getAccountUid());
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
            criteria.andLike("username", likeName)
                    .orLike("displayName", likeName)
                    .orLike("email", likeName);
        }
        if (pageQuery.getIsActive() != null){
            criteria.andEqualTo("isActive", pageQuery.getIsActive());
        }
        example.setOrderByClause("create_time");
        List<DatasourceAccount> data = dsAccountMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DatasourceAccount getByUniqueKey(String accountUid, String accountId) {
        Example example = new Example(DatasourceAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountUid", accountUid)
                .andEqualTo("accountId", accountId);
        return dsAccountMapper.selectOneByExample(example);
    }

    @Override
    public void add(DatasourceAccount datasourceAccount) {
        dsAccountMapper.insert(datasourceAccount);
    }

    @Override
    public void update(DatasourceAccount datasourceAccount) {
        dsAccountMapper.updateByPrimaryKey(datasourceAccount);
    }
}
