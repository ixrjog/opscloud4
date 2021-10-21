package com.baiyi.opscloud.ldap.provider;

import com.baiyi.opscloud.common.datasource.LdapDsInstanceConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.auth.BaseAuthProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.ldap.handler.LdapHandler;
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

    private LdapDsInstanceConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, LdapDsInstanceConfig.class).getLdap();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }


}

