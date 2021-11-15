package com.baiyi.opscloud.core.provider.auth;

import com.baiyi.opscloud.core.provider.base.auth.SimpleAuthenticationProvider;
import com.baiyi.opscloud.core.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.core.factory.AuthProviderFactory;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.model.Authorization;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/23 9:36 上午
 * @Version 1.0
 */
public abstract class BaseAuthProvider extends SimpleDsInstanceProvider implements SimpleAuthenticationProvider, InitializingBean {

    @Resource
    protected DsConfigHelper dsConfigHelper;

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
