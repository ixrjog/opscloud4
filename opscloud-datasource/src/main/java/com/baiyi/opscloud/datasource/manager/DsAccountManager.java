package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.business.account.AccountHandlerFactory;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.datasource.manager.base.IManager;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
@RequiredArgsConstructor
public class DsAccountManager extends BaseManager implements IManager<User> {

    /**
     * 支持账户管理的实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.LDAP, DsTypeEnum.ZABBIX, DsTypeEnum.ALIYUN, DsTypeEnum.AWS};

    private final StringEncryptor stringEncryptor;

    private final NoticeManager noticeManager;

    @Override
    protected DsTypeEnum[] getFilterInstanceTypes() {
        return FILTER_INSTANCE_TYPES;
    }

    @Override
    protected String getTag() {
        return TagConstants.ACCOUNT.getTag();
    }

    /**
     * 解密
     *
     * @param user
     * @return
     */
    private void decrypt(User user) {
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(stringEncryptor.decrypt(user.getPassword()));
        }
    }

    @Override
    public void create(User user) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源账户管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        decrypt(user);
        instances.forEach(e -> AccountHandlerFactory.getByInstanceType(e.getInstanceType()).create(e, user));
        noticeManager.sendMessage(user, NoticeManager.MsgKeys.CREATE_USER);
    }

    @Override
    public void update(User user) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源账户管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        decrypt(user);
        instances.forEach(e -> AccountHandlerFactory.getByInstanceType(e.getInstanceType()).update(e, user));
        // 非修改密码不发通知
        if (!StringUtils.isEmpty(user.getPassword())) {
            noticeManager.sendMessage(user, NoticeManager.MsgKeys.UPDATE_USER_PASSWORD);
        }
    }

    @Override
    public void delete(User user) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源账户管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> AccountHandlerFactory.getByInstanceType(e.getInstanceType()).delete(e, user));
    }

    @Override
    public void grant(User user, BaseBusiness.IBusiness businessResource) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源账户管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> AccountHandlerFactory.getByInstanceType(e.getInstanceType()).grant(e, user,
                businessResource));
    }

    @Override
    public void revoke(User user, BaseBusiness.IBusiness businessResource) {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            log.info("{} 数据源账户管理: 无可用实例", this.getClass().getSimpleName());
            return;
        }
        instances.forEach(e -> AccountHandlerFactory.getByInstanceType(e.getInstanceType()).revoke(e, user,
                businessResource));
    }

}
