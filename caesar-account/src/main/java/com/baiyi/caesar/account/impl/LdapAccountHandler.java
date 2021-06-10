package com.baiyi.caesar.account.impl;

import com.baiyi.caesar.account.IAccount;
import com.baiyi.caesar.account.builder.AccountBuilder;
import com.baiyi.caesar.common.datasource.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.vo.datasource.DatasourceInstanceVO;
import com.baiyi.caesar.ldap.repo.PersonRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/10 10:32 上午
 * @Version 1.0
 */
@Component
@Primary
public class LdapAccountHandler extends BaseAccountHandler implements IAccount {

    // @Async(value = Global.TaskPools.EXECUTOR)

    @Resource
    private PersonRepo personRepo;

    @Override
    //@Async
    public void pullAccount(DatasourceInstanceVO.Instance dsInstance) {
        doPullAccount(dsInstance);
    }

    protected BaseDsInstanceConfig getConfig(Integer dsConfigId) {
        DatasourceConfig datasourceConfig = dsConfigService.getById(dsConfigId);
        return dsFactory.build(datasourceConfig, LdapDsInstanceConfig.class);
    }

    protected List<DatasourceAccount> listAccount(BaseDsInstanceConfig baseDsInstanceConfig) {
        LdapDsInstanceConfig ldapDsInstanceConfig = (LdapDsInstanceConfig) baseDsInstanceConfig;
        return personRepo.getPersonList(ldapDsInstanceConfig.getLdap()).stream().map(AccountBuilder::build).collect(Collectors.toList());
    }

    protected void wrap(DatasourceInstanceVO.Instance dsInstance, DatasourceAccount account) {
        account.setAccountUid(dsInstance.getUuid());
        account.setAccountId(account.getUsername());
    }

}
