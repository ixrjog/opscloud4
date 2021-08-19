package com.baiyi.opscloud.datasource.account.impl.base;

import com.baiyi.opscloud.datasource.account.AccountProviderFactory;
import com.baiyi.opscloud.datasource.account.IAccount;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.service.user.UserPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/11 2:05 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAccountProvider<T> extends SimpleDsInstanceProvider implements IAccount, InitializingBean {

    @Resource
    protected DsConfigFactory dsConfigFactory;

    @Resource
    private UserPermissionService userPermissionService;

    protected List<UserPermission> queryUserPermission(User user, Integer businessType) {
        return userPermissionService.queryByUserPermission(user.getId(), businessType);
    }

    protected abstract T buildConfig(DatasourceConfig dsConfig);

    @Override
    public void create(DatasourceInstance dsInstance, User user) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doCreate(buildConfig(context.getDsConfig()), user);
    }

    @Override
    public void update(DatasourceInstance dsInstance, User user) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doUpdate(buildConfig(context.getDsConfig()), user);
    }

    @Override
    public void delete(DatasourceInstance dsInstance, User user) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doDelete(buildConfig(context.getDsConfig()), user);
    }

    @Override
    public void grant(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        if (getBusinessResourceType() == businessResource.getBusinessType())
            doGrant(buildConfig(context.getDsConfig()), user, businessResource);
    }

    @Override
    public void revoke(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        if (getBusinessResourceType() == businessResource.getBusinessType())
            doRevoke(buildConfig(context.getDsConfig()), user, businessResource);
    }

    protected abstract void doCreate(T t, User user);

    protected abstract void doUpdate(T t, User user);

    protected abstract void doDelete(T t, User user);

    protected abstract void doGrant(T t, User user, BaseBusiness.IBusiness businessResource);

    protected abstract void doRevoke(T t, User user, BaseBusiness.IBusiness businessResource);

    protected abstract int getBusinessResourceType();

    @Override
    public void afterPropertiesSet() {
        AccountProviderFactory.register(this);
    }
}
