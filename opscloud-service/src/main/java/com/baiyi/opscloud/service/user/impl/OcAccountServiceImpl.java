package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.mapper.opscloud.OcAccountMapper;
import com.baiyi.opscloud.service.user.OcAccountService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:43 下午
 * @Version 1.0
 */
@Service
public class OcAccountServiceImpl implements OcAccountService {

    @Resource
    private OcAccountMapper ocAccountMapper;

    @Override
    public OcAccount queryOcAccountByAccountId(int accountType, String accountId) {
        Example example = new Example(OcAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountType", accountType);
        criteria.andEqualTo("accountId", accountId);
        return ocAccountMapper.selectOneByExample(example);
    }

    @Override
    public List<OcAccount> queryOcAccountByAccountType(int accountType) {
        Example example = new Example(OcAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountType", accountType);
        return ocAccountMapper.selectByExample(example);
    }

    @Override
    public OcAccount queryOcAccountByUsername(int accountType, String username) {
        Example example = new Example(OcAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("accountType", accountType);
        criteria.andEqualTo("username", username);
        return ocAccountMapper.selectOneByExample(example);
    }

    @Override
    public void delOcAccount(int id) {
        ocAccountMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void addOcAccount(OcAccount ocAccount) {
        ocAccountMapper.insert(ocAccount);
    }

    @Override
    public OcAccount queryOcAccount(int id) {
        return ocAccountMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateOcAccount(OcAccount ocAccount) {
        ocAccountMapper.updateByPrimaryKey(ocAccount);
    }

}
