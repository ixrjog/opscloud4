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
import com.baiyi.opscloud.service.user.UserPermissionService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/8/11 2:05 下午
 * @Version 1.0
 */
public abstract class BaseAccountProvider<T> extends SimpleDsInstanceProvider implements IAccount, InitializingBean {

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    protected DsConfigFactory dsConfigFactory;

    @Resource
    private UserPermissionService userPermissionService;

    protected List<UserPermission> queryUserPermission(User user, Integer businessType) {
        return userPermissionService.queryByUserPermission(user.getId(), businessType);
    }

    protected abstract T buildConfig(DatasourceConfig dsConfig);

    /**
     * 解密
     *
     * @param user
     * @return
     */
//    private User decrypt(User user) {
//        if (!StringUtils.isEmpty(user.getPassword()))
//            user.setPassword(stringEncryptor.decrypt(user.getPassword()));
//        return user;
//    }

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

    protected abstract void doCreate(T t, User user);

    protected abstract void doUpdate(T t, User user);

    protected abstract void doDelete(T t, User user);

    @Override
    public void afterPropertiesSet() {
        AccountProviderFactory.register(this);
    }
}
