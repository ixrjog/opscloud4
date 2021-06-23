package com.baiyi.caesar.ldap.provider;

import com.baiyi.caesar.common.datasource.LdapDsInstanceConfig;
import com.baiyi.caesar.common.datasource.config.DsLdapConfig;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.auth.BaseAuthProvider;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.domain.model.Authorization;
import com.baiyi.caesar.ldap.handler.LdapHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:33 上午
 * @Version 1.0
 */
@Component
public class LdapAuthProvider extends BaseAuthProvider {

    @Resource
    private LdapHandler ldapHandler;

    @Override
    protected boolean auth(DsInstanceContext dsInstanceContext, Authorization.Credential credential) {
        return ldapHandler.loginCheck(buildConfig(dsInstanceContext.getDsConfig()), credential);
    }

    private DsLdapConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, LdapDsInstanceConfig.class).getLdap();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }


}

