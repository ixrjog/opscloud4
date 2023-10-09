package com.baiyi.opscloud.datasource.business.server.impl.base;

import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.datasource.business.server.IServer;
import com.baiyi.opscloud.datasource.business.server.factory.ServerHandlerFactory;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.service.business.BizPropertyHelper;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:28 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseServerHandler<T> extends SimpleDsInstanceProvider implements IServer, InitializingBean {

    @Resource
    protected DsConfigManager dsConfigManager;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private EnvService envService;

    @Resource
    private BizPropertyHelper bizPropertyHelper;

    protected static ThreadLocal<DsInstanceContext> dsInstanceContext = new ThreadLocal<>();

    protected abstract void initialConfig(DatasourceConfig dsConfig);

    private void pre(DatasourceInstance dsInstance) {
        dsInstanceContext.set(buildDsInstanceContext(dsInstance.getId()));
        initialConfig(dsInstanceContext.get().getDsConfig());
    }

    protected Env getEnv(Server server) {
        return envService.getByEnvType(server.getEnvType());
    }

    protected ServerProperty.Server getBusinessProperty(Server server) {
        return bizPropertyHelper.getBusinessProperty(server);
    }

    protected ServerGroup getServerGroup(Server server) {
        return serverGroupService.getById(server.getServerGroupId());
    }

    @Override
    public void create(DatasourceInstance dsInstance, Server server) {
        pre(dsInstance);
        doCreate(server);
    }

    @Override
    public void update(DatasourceInstance dsInstance, Server server) {
        pre(dsInstance);
        doUpdate(server);
    }

    @Override
    public void delete(DatasourceInstance dsInstance, Server server) {
        pre(dsInstance);
        doDelete(server);
    }

    @Override
    public void grant(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    public void revoke(DatasourceInstance dsInstance, User user, BaseBusiness.IBusiness businessResource) {
    }

    protected abstract void doCreate(Server server);

    protected abstract void doUpdate(Server server);

    protected abstract void doDelete(Server server);

    protected abstract void doGrant(User user, BaseBusiness.IBusiness businessResource);

    protected abstract void doRevoke(User user, BaseBusiness.IBusiness businessResource);

    protected abstract int getBusinessResourceType();

    @Override
    public void afterPropertiesSet() {
        ServerHandlerFactory.register(this);
    }

}