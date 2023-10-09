package com.baiyi.opscloud.datasource.business.account.impl.base;

import com.baiyi.opscloud.datasource.business.account.AccountGroupHandlerFactory;
import com.baiyi.opscloud.datasource.business.account.IAccountGroup;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/9/14 5:42 下午
 * @Version 1.0
 */
public abstract class AbstractAccountGroupHandler extends SimpleDsInstanceProvider implements IAccountGroup, InitializingBean {

    @Resource
    protected DsConfigManager dsConfigManager;

    protected static ThreadLocal<DsInstanceContext> dsInstanceContext = new ThreadLocal<>();

    protected abstract void initialConfig(DatasourceConfig dsConfig);

    private void pre(DatasourceInstance dsInstance) {
        dsInstanceContext.set(buildDsInstanceContext(dsInstance.getId()));
        initialConfig(dsInstanceContext.get().getDsConfig());
    }

    @Override
    public void create(DatasourceInstance dsInstance, UserGroup userGroup) {
        pre(dsInstance);
        doCreate(userGroup);
    }

    @Override
    public void update(DatasourceInstance dsInstance, UserGroup userGroup) {
    }

    @Override
    public void delete(DatasourceInstance dsInstance, UserGroup userGroup) {
    }

    @Override
    public void grant(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
        if (getBusinessResourceType() == businessResource.getBusinessType()) {
            pre(dsInstance);
            doGrant(user, businessResource);
        }
    }

    @Override
    public void revoke(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
        if (getBusinessResourceType() == businessResource.getBusinessType()) {
            pre(dsInstance);
            doRevoke(user, businessResource);
        }
    }

    protected abstract void doCreate(UserGroup userGroup);

    protected abstract void doUpdate(UserGroup userGroup);

    protected abstract void doDelete(UserGroup userGroup);

    public abstract void doGrant(User user, BaseBusiness.IBusiness businessResource);

    public abstract void doRevoke(User user, BaseBusiness.IBusiness businessResource);

    protected abstract int getBusinessResourceType();

    @Override
    public void afterPropertiesSet() {
        AccountGroupHandlerFactory.register(this);
    }
}

