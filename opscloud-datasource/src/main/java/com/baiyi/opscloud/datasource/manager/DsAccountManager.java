package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.account.AccountProviderFactory;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.datasource.manager.base.IManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 账户管理（同步数据源实例账户信息）    ··
 *
 * @Author baiyi
 * @Date 2021/8/11 11:09 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class DsAccountManager extends BaseManager implements IManager<User> {

    private static final String ACCOUNT_TAG = "Account";

    /**
     * 支持账户管理的实例类型
     */
    private static final DsTypeEnum[] accountInstanceTypes = {DsTypeEnum.LDAP, DsTypeEnum.ZABBIX};

    @Override
    protected DsTypeEnum[] getDsTypes() {
        return accountInstanceTypes;
    }

    @Override
    protected String getTag() {
        return ACCOUNT_TAG;
    }

    @Override
    public void create(User user) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("DsAccountManager数据源账户管理: 无可用实例");
            return;
        }
        instances.forEach(e -> AccountProviderFactory.getIAccountByInstanceType(e.getInstanceType()).create(e, user));
    }

    @Override
    // @Async(value = Global.TaskPools.EXECUTOR)
    public void update(User user) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("DsAccountManager数据源账户管理: 无可用实例");
            return;
        }
        instances.forEach(e -> AccountProviderFactory.getIAccountByInstanceType(e.getInstanceType()).update(e, user));
    }

    @Override
    // @Async(value = Global.TaskPools.EXECUTOR)
    public void delete(User user) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("DsAccountManager数据源账户管理: 无可用实例");
            return;
        }
        instances.forEach(e -> AccountProviderFactory.getIAccountByInstanceType(e.getInstanceType()).delete(e, user));
    }

}