package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.business.server.factory.ServerGroupHandlerFactory;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.datasource.manager.base.IManager;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/24 11:24 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class DsServerGroupManager extends BaseManager implements IManager<ServerGroup> {

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
    public void create(ServerGroup serverGroup) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源服务器组管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> ServerGroupHandlerFactory.getByInstanceType(e.getInstanceType()).create(e, serverGroup));
    }

    @Override
    public void update(ServerGroup serverGroup) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源服务器组管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> ServerGroupHandlerFactory.getByInstanceType(e.getInstanceType()).update(e, serverGroup));
    }

    @Override
    public void delete(ServerGroup serverGroup) {
        // TODO
    }

    @Override
    public void grant(User user, BaseBusiness.IBusiness businessResource) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源服务器组管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> ServerGroupHandlerFactory.getByInstanceType(e.getInstanceType()).grant(e, user, businessResource));
    }

    @Override
    public void revoke(User user, BaseBusiness.IBusiness businessResource) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源服务器组管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> ServerGroupHandlerFactory.getByInstanceType(e.getInstanceType()).revoke(e, user, businessResource));
    }

}
