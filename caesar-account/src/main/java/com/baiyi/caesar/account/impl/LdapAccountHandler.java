package com.baiyi.caesar.account.impl;

import com.baiyi.caesar.account.IAccount;
import com.baiyi.caesar.account.builder.AccountBuilder;
import com.baiyi.caesar.account.builder.AccountGroupBuilder;
import com.baiyi.caesar.account.factory.AccountHandlerFactory;
import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountGroup;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;
import com.baiyi.caesar.ldap.repo.GroupRepo;
import com.baiyi.caesar.ldap.repo.PersonRepo;
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
public class LdapAccountHandler extends BaseAccountHandler implements IAccount {

    @Resource
    private PersonRepo personRepo;

    @Resource
    private GroupRepo groupRepo;

    /**
     * 启用AOP，注入自己
     */
    @Resource
    private LdapAccountHandler ldapAccountHandler;

    @Override
    @SingleTask(name = "PullLdapAccount", lockSecond = 5 * 60)
    public void pullAccount(DsInstanceVO.Instance dsInstance) {
        doPullAccount(dsInstance);
    }

    @Override
    @SingleTask(name = "PullLdapAccountGroup", lockSecond = 5 * 60)
    public void pullAccountGroup(DsInstanceVO.Instance dsInstance) {
        doPullGroup(dsInstance);
    }

    protected BaseDsInstanceConfig getConfig(Integer dsConfigId) {
        DatasourceConfig datasourceConfig = dsConfigService.getById(dsConfigId);
        return dsFactory.build(datasourceConfig, LdapDsInstanceConfig.class);
    }

    protected List<DatasourceAccount> listAccount(BaseDsInstanceConfig baseDsInstanceConfig) {
        LdapDsInstanceConfig ldapDsInstanceConfig = (LdapDsInstanceConfig) baseDsInstanceConfig;
        return personRepo.getPersonList(ldapDsInstanceConfig.getLdap()).stream().map(AccountBuilder::build).collect(Collectors.toList());
    }

    protected List<DatasourceAccountGroup> listGroup(BaseDsInstanceConfig baseDsInstanceConfig) {
        LdapDsInstanceConfig ldapDsInstanceConfig = (LdapDsInstanceConfig) baseDsInstanceConfig;
        return groupRepo.getGroupList(ldapDsInstanceConfig.getLdap()).stream().map(AccountGroupBuilder::build).collect(Collectors.toList());
    }

    protected List<String> listGroupRelationships(BaseDsInstanceConfig baseDsInstanceConfig, String groupName) {
        return groupRepo.queryGroupMember(((LdapDsInstanceConfig) baseDsInstanceConfig).getLdap(), groupName);
    }

    protected void wrap(DsInstanceVO.Instance dsInstance, DatasourceAccount account) {
        account.setAccountUid(dsInstance.getUuid());
        account.setAccountId(account.getUsername());
    }

    protected void wrap(DsInstanceVO.Instance dsInstance, DatasourceAccountGroup group) {
        group.setAccountUid(dsInstance.getUuid());
        group.setAccountId(group.getName());
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        AccountHandlerFactory.register(ldapAccountHandler);
    }

}
