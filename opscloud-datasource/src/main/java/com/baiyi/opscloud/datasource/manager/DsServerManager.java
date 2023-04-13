package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.datasource.manager.base.IManager;
import com.baiyi.opscloud.datasource.business.server.factory.ServerHandlerFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/19 11:00 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class DsServerManager extends BaseManager implements IManager<Server> {

    /**
     * 过滤实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.ZABBIX};

    @Override
    protected DsTypeEnum[] getFilterInstanceTypes() {
        return FILTER_INSTANCE_TYPES;
    }

    @Override
    protected String getTag() {
        return TagConstants.SERVER.getTag();
    }

    @Override
    public void create(Server server) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源服务器管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> ServerHandlerFactory.getByInstanceType(e.getInstanceType()).create(e, server));
    }

    @Override
    public void update(Server server) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源服务器管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> ServerHandlerFactory.getByInstanceType(e.getInstanceType()).update(e, server));
    }

    @Override
    // @Async(value = Global.TaskPools.EXECUTOR)
    public void delete(Server server) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源服务器管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> ServerHandlerFactory.getByInstanceType(e.getInstanceType()).delete(e, server));
    }

    @Override
    public void grant(User user, BaseBusiness.IBusiness businessResource) {
    }

    @Override
    public void revoke(User user, BaseBusiness.IBusiness businessResource) {
    }

}
