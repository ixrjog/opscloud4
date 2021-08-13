package com.baiyi.opscloud.datasource.account.impl;

import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsLdapConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.account.convert.AccountConvert;
import com.baiyi.opscloud.datasource.account.impl.base.BaseAccountProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/11 2:09 下午
 * @Version 1.0
 */
@Component
public class LdapAccountProvider extends BaseAccountProvider<DsLdapConfig.Ldap> {

    @Resource
    private PersonRepo personRepo;

    @Override
    protected void doCreate(DsLdapConfig.Ldap ldap, User user) {
        if (!personRepo.checkPersonInLdap(ldap, user.getUsername()))
            personRepo.create(ldap, AccountConvert.toLdapPerson(user));
    }

    @Override
    protected void doUpdate(DsLdapConfig.Ldap ldap, User user) {
        if (personRepo.checkPersonInLdap(ldap, user.getUsername()))
            personRepo.update(ldap, AccountConvert.toLdapPerson(user));
    }

    @Override
    protected void doDelete(DsLdapConfig.Ldap ldap, User user) {
        if (personRepo.checkPersonInLdap(ldap, user.getUsername()))
            personRepo.delete(ldap, user.getUsername());
    }

    @Override
    protected DsLdapConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, LdapDsInstanceConfig.class).getLdap();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.getName();
    }
}
