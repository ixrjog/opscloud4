package com.baiyi.opscloud.datasource.business.account.impl;

import com.baiyi.opscloud.datasource.business.account.converter.AccountConverter;
import com.baiyi.opscloud.datasource.business.account.impl.base.AbstractZabbixAccountHandler;
import com.baiyi.opscloud.datasource.business.account.util.ZabbixMediaUtil;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5UserDriver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/11 4:31 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZabbixAccountHandler extends AbstractZabbixAccountHandler {

    private final ZabbixV5UserDriver zabbixV5UserDriver;

    public static final String ZABBIX_DEFAULT_USERGROUP = "users_default";

    private void postCacheEvict(com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User zabbixUser, User user) {
        zabbixV5UserDriver.evictById(configContext.get(), zabbixUser.getUserid());
        zabbixV5UserDriver.evictByUsername(configContext.get(), user.getUsername());
    }

    @Override
    protected void doCreate(User user) {
        zabbixV5UserDriver.create(configContext.get(), AccountConverter.toZabbixUser(user), ZabbixMediaUtil.buildMedias(user), getUsrgrps(configContext.get(), user));
        log.info("创建Zabbix用户: username={}", user.getUsername());
    }

    @Override
    protected void doUpdate(User user) {
       com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User zabbixUser = zabbixV5UserDriver.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) {
            doCreate(user);
        } else {
            com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User updateUser = AccountConverter.toZabbixUser(user);
            updateUser.setUserid(zabbixUser.getUserid());
            zabbixV5UserDriver.update(configContext.get(), updateUser, getUsrgrps(configContext.get(), user), ZabbixMediaUtil.buildMedias(user));
            // 清除缓存
            postCacheEvict(zabbixUser,user);
            log.info("更新Zabbix用户: username={}", user.getUsername());
        }
    }

    @Override
    protected void doDelete(User user) {
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User zabbixUser = zabbixV5UserDriver.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) {
            return;
        }
        zabbixV5UserDriver.delete(configContext.get(), user.getUsername());
        // 清除缓存
        postCacheEvict(zabbixUser,user);
        log.info("删除Zabbix用户: username={}", user.getUsername());
    }

    @Override
    public void doGrant(User user, BaseBusiness.IBusiness businessResource) {
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User zabbixUser = zabbixV5UserDriver.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) {
            doCreate(user);
        } else {
            zabbixV5UserDriver.update(configContext.get(), zabbixUser, getUsrgrps(configContext.get(), user));
            // 清除缓存
            postCacheEvict(zabbixUser,user);
            log.info("更新Zabbix用户: username={}", user.getUsername());
        }
    }

    @Override
    public void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
        doGrant(user, businessResource);
    }

}
