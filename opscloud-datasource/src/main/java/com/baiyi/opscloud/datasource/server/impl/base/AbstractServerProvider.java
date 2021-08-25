package com.baiyi.opscloud.datasource.server.impl.base;

import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.datasource.server.IServer;
import com.baiyi.opscloud.datasource.server.factory.ServerProviderFactory;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.service.business.BusinessPropertyHelper;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:28 上午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractServerProvider<T> extends SimpleDsInstanceProvider implements IServer, InitializingBean {

    @Resource
    protected DsConfigFactory dsConfigFactory;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private EnvService envService;

    @Resource
    private BusinessPropertyHelper businessPropertyHelper;

    protected abstract T buildConfig(DatasourceConfig dsConfig);

    protected Env getEnv(Server server) {
        return envService.getByEnvType(server.getEnvType());
    }

    protected ServerProperty.Server getBusinessProperty(Server server) {
        return businessPropertyHelper.getBusinessProperty(server);
    }

    protected ServerGroup getServerGroup(Server server) {
        return serverGroupService.getById(server.getServerGroupId());
    }

    @Override
    public void create(DatasourceInstance dsInstance, Server server) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doCreate(buildConfig(context.getDsConfig()), server);
    }

    @Override
    public void update(DatasourceInstance dsInstance, Server server) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doUpdate(buildConfig(context.getDsConfig()), server);
    }

    @Override
    public void delete(DatasourceInstance dsInstance, Server server) {
        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
        doDelete(buildConfig(context.getDsConfig()), server);
    }

//    @Override
//    public void add(DatasourceInstance dsInstance, Server server, BaseBusiness.IBusiness businessResource) {
//        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
//        if (getBusinessResourceType() == businessResource.getBusinessType())
//            doGrant(buildConfig(context.getDsConfig()), server, businessResource);
//    }
//
//    @Override
//    public void remove(DatasourceInstance dsInstance, Server server, BaseBusiness.IBusiness businessResource) {
//        DsInstanceContext context = buildDsInstanceContext(dsInstance.getId());
//        if (getBusinessResourceType() == businessResource.getBusinessType())
//            doRevoke(buildConfig(context.getDsConfig()), server, businessResource);
//    }

    @Override
    public void grant(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    public void revoke(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
    }

    protected abstract void doCreate(T t, Server server);

    protected abstract void doUpdate(T t, Server server);

    protected abstract void doDelete(T t, Server server);

    protected abstract void doGrant(T t, User user, BaseBusiness.IBusiness businessResource);

    protected abstract void doRevoke(T t, User user, BaseBusiness.IBusiness businessResource);

    protected abstract int getBusinessResourceType();

    @Override
    public void afterPropertiesSet() {
        ServerProviderFactory.register(this);
    }

}