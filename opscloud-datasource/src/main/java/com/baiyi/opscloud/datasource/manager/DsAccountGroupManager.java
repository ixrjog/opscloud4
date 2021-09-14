package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.accountGroup.AccountGroupProviderFactory;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.datasource.manager.base.IManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/14 5:15 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class DsAccountGroupManager extends BaseManager implements IManager<UserGroup> {

    private static final String ACCOUNT_TAG = "Account";

    /**
     * 支持账户管理的实例类型
     */
    private static final DsTypeEnum[] filterInstanceTypes = {DsTypeEnum.LDAP};

    @Override
    protected DsTypeEnum[] getFilterInstanceTypes() {
        return filterInstanceTypes;
    }

    @Override
    protected String getTag() {
        return ACCOUNT_TAG;
    }

    @Override
    public void create(UserGroup userGroup) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源账户组管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> AccountGroupProviderFactory.getIAccountGroupByInstanceType(e.getInstanceType()).create(e, userGroup));
    }

    @Override
    public void update(UserGroup userGroup) {
        // 不处理更新 用户组不允许变更
    }

    @Override
    public void delete(UserGroup userGroup) {
        // 不处理删除
    }

    @Override
    public void grant(User user, BaseBusiness.IBusiness businessResource) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源账户组管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> AccountGroupProviderFactory.getIAccountGroupByInstanceType(e.getInstanceType()).grant(e, user, businessResource));
    }

    @Override
    public void revoke(User user, BaseBusiness.IBusiness businessResource) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源账户组管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> AccountGroupProviderFactory.getIAccountGroupByInstanceType(e.getInstanceType()).revoke(e, user, businessResource));
    }

}