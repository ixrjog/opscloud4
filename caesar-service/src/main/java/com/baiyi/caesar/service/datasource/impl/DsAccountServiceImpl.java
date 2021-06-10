package com.baiyi.caesar.service.datasource.impl;

import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.mapper.caesar.DatasourceAccountMapper;
import com.baiyi.caesar.service.datasource.DsAccountService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

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
    public DatasourceAccount getByUniqueKey(String accountUid,String accountId){
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
