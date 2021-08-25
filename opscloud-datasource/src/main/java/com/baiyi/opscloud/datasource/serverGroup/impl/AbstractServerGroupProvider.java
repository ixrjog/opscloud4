package com.baiyi.opscloud.datasource.serverGroup.impl;

import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.datasource.serverGroup.IServerGroup;
import com.baiyi.opscloud.datasource.serverGroup.factory.ServerGroupProviderFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/24 1:44 下午
 * @Version 1.0
 */
public abstract class AbstractServerGroupProvider<T> extends SimpleDsInstanceProvider implements IServerGroup, InitializingBean {

    @Resource
    protected DsConfigFactory dsConfigFactory;


    protected abstract T buildConfig(DatasourceConfig dsConfig);

    @Override
    public void create(DatasourceInstance dsInstance, ServerGroup serverGroup) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doCreate(buildConfig(context.getDsConfig()), serverGroup);
    }

    @Override
    public void update(DatasourceInstance dsInstance, ServerGroup serverGroup) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doUpdate(buildConfig(context.getDsConfig()), serverGroup);
    }

    @Override
    public void delete(DatasourceInstance dsInstance, ServerGroup serverGroup) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doDelete(buildConfig(context.getDsConfig()), serverGroup);
    }

    @Override
    public void grant(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    public void revoke(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
    }

    protected abstract void doCreate(T t, ServerGroup serverGroup);

    protected abstract void doUpdate(T t, ServerGroup serverGroup);

    protected abstract void doDelete(T t, ServerGroup serverGroup);

    protected abstract void doGrant(T t, User user, BaseBusiness.IBusiness businessResource);

    protected abstract void doRevoke(T t, User user, BaseBusiness.IBusiness businessResource);

    protected abstract int getBusinessResourceType();

    @Override
    public void afterPropertiesSet() {
        ServerGroupProviderFactory.register(this);
    }

}