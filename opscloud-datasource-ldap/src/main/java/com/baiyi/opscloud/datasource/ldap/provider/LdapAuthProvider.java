package com.baiyi.opscloud.datasource.ldap.provider;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.auth.BaseAuthProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.datasource.ldap.driver.LdapDriver;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:33 上午
 * @Version 1.0
 */
@Component
public class LdapAuthProvider extends BaseAuthProvider {

    @Resource
    private LdapDriver ldapDriver;

    @Override
    protected boolean auth(DsInstanceContext dsInstanceContext, Authorization.Credential credential) {
        return ldapDriver.verifyLogin(buildConfig(dsInstanceContext.getDsConfig()), credential);
    }

    private LdapConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, LdapConfig.class).getLdap();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }

}