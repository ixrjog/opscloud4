package com.baiyi.caesar.datasource.auth;

import com.baiyi.caesar.datasource.base.auth.SimpleAuthenticationProvider;
import com.baiyi.caesar.datasource.base.common.SimpleDsInstanceProvider;
import com.baiyi.caesar.datasource.factory.AuthProviderFactory;
import com.baiyi.caesar.datasource.factory.DsConfigFactory;
import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.domain.model.Authorization;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:36 上午
 * @Version 1.0
 */
public abstract class BaseAuthProvider extends SimpleDsInstanceProvider implements SimpleAuthenticationProvider, InitializingBean {

    @Resource
    protected DsConfigFactory dsFactory;

    @Override
    public boolean login(DatasourceInstance instance, Authorization.Credential credential) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(instance.getId());
        return auth(dsInstanceContext, credential);
    }

    protected abstract boolean auth(DsInstanceContext dsInstanceContext, Authorization.Credential credential);

    @Override
    public void afterPropertiesSet() {
        AuthProviderFactory.register(this); // 工厂中注册
    }

}
