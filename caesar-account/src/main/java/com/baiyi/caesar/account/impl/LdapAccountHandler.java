package com.baiyi.caesar.account.impl;

import com.baiyi.caesar.account.IAccount;
import com.baiyi.caesar.account.builder.AccountBuilder;
import com.baiyi.caesar.account.builder.AccountGroupBuilder;
import com.baiyi.caesar.account.factory.AccountHandlerFactory;
import com.baiyi.caesar.account.packer.UserPacker;
import com.baiyi.caesar.common.annotation.SingleTask;
import com.baiyi.caesar.common.datasource.BaseDsInstanceConfig;
import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.LdapDsConfig;
import com.baiyi.caesar.domain.annotation.Decrypt;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccount;
import com.baiyi.caesar.domain.generator.caesar.DatasourceAccountGroup;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.model.Authorization;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;
import com.baiyi.caesar.ldap.handler.LdapHandler;
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

    @Resource
    private LdapHandler ldapHandler;

    /**
     * 启用AOP，注入自己
     */
    @Resource
    private LdapAccountHandler ldapAccountHandler;

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        AccountHandlerFactory.register(ldapAccountHandler);
    }

    @Override
    @SingleTask(name = "PullLdapAccount", lockTime = 5 * 60)
    public void pullAccount(DsInstanceVO.Instance dsInstance) {
        doPullAccount(dsInstance);
    }

    @Override
    @SingleTask(name = "PullLdapAccountGroup", lockTime = 5 * 60)
    public void pullAccountGroup(DsInstanceVO.Instance dsInstance) {
        doPullGroup(dsInstance);
    }

    protected BaseDsInstanceConfig getConfig(Integer dsConfigId) {
        DatasourceConfig datasourceConfig = dsConfigService.getById(dsConfigId);
        return dsFactory.build(datasourceConfig, LdapDsInstanceConfig.class);
    }

    private LdapDsConfig.Ldap toLdapConfig(BaseDsInstanceConfig baseDsInstanceConfig) {
        return ((LdapDsInstanceConfig) baseDsInstanceConfig).getLdap();
    }

    protected List<DatasourceAccount> listAccount(BaseDsInstanceConfig baseDsInstanceConfig) {
        return personRepo.getPersonList(toLdapConfig(baseDsInstanceConfig)).stream().map(AccountBuilder::build).collect(Collectors.toList());
    }

    protected List<DatasourceAccountGroup> listGroup(BaseDsInstanceConfig baseDsInstanceConfig) {
        return groupRepo.getGroupList(toLdapConfig(baseDsInstanceConfig)).stream().map(AccountGroupBuilder::build).collect(Collectors.toList());
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

    public boolean authentication(DsInstanceVO.Instance dsInstance, Authorization.Credential credential) {
        BaseDsInstanceConfig baseDsInstanceConfig = getConfig(dsInstance.getConfigId());
        return ldapHandler.loginCheck(toLdapConfig(baseDsInstanceConfig), credential);
    }

    @Override
    public void update(DsInstanceVO.Instance dsInstance, User user) {
        BaseDsInstanceConfig baseDsInstanceConfig = getConfig(dsInstance.getConfigId());
        try {
            personRepo.update(toLdapConfig(baseDsInstanceConfig), UserPacker.toPerson(user));
        } catch (Exception ignored) {
        }
    }

    @Decrypt // 解密密码字段
    @Override
    public void create(DsInstanceVO.Instance dsInstance, User user) {
        BaseDsInstanceConfig baseDsInstanceConfig = getConfig(dsInstance.getConfigId());
        user.setSource("ldap");
        personRepo.create(toLdapConfig(baseDsInstanceConfig), UserPacker.toPerson(user));
    }

    @Override
    public void delete(DsInstanceVO.Instance dsInstance, User user) {
        BaseDsInstanceConfig baseDsInstanceConfig = getConfig(dsInstance.getConfigId());
        personRepo.delete(toLdapConfig(baseDsInstanceConfig), user.getUsername());
    }

}
