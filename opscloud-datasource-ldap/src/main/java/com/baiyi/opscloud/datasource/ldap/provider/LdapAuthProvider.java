package com.baiyi.opscloud.datasource.ldap.provider;

import com.baiyi.opscloud.common.datasource.LdapConfig;
import com.baiyi.opscloud.common.constant.enums.DsTypeEnum;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.auth.BaseAuthProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.datasource.ldap.datasource.LdapDatasource;
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
    private LdapDatasource ldapDatasource;

    @Override
    protected boolean auth(DsInstanceContext dsInstanceContext, Authorization.Credential credential) {
        return ldapDatasource.loginCheck(buildConfig(dsInstanceContext.getDsConfig()), credential);
    }

    private LdapConfig.Ldap buildConfig(DatasourceConfig dsConfig) {
        return dsConfigHelper.build(dsConfig, LdapConfig.class).getLdap();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.name();
    }


}

