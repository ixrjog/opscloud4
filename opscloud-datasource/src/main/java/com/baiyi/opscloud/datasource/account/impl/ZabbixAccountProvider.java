package com.baiyi.opscloud.datasource.account.impl;

import com.baiyi.opscloud.datasource.account.convert.AccountConvert;
import com.baiyi.opscloud.datasource.account.impl.base.BaseZabbixAccountProvider;
import com.baiyi.opscloud.datasource.account.util.ZabbixMediaUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.handler.ZabbixUserHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/11 4:31 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixAccountProvider extends BaseZabbixAccountProvider {

    @Resource
    private ZabbixUserHandler zabbixUserHandler;

    public static final String ZABBIX_DEFAULT_USERGROUP = "users_default";

    @Override
    protected void doCreate(User user) {
        zabbixUserHandler.create(configContext.get(), AccountConvert.toZabbixUser(user), ZabbixMediaUtil.buildMedias(user), getUsrgrps(configContext.get(), user));
        log.info("创建Zabbix用户: username = {}", user.getUsername());
    }

    @Override
    protected void doUpdate(User user) {
        ZabbixUser zabbixUser = zabbixUserHandler.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) {
            doCreate(user);
        } else {
            ZabbixUser updateUser = AccountConvert.toZabbixUser(user);
            updateUser.setUserid(zabbixUser.getUserid());
            zabbixUserHandler.update(configContext.get(), updateUser, getUsrgrps(configContext.get(), user), ZabbixMediaUtil.buildMedias(user));
            // 清除缓存
            zabbixUserHandler.evictById(zabbixUser.getUserid());
            zabbixUserHandler.evictByUsername(user.getUsername());
            log.info("更新Zabbix用户: username = {}", user.getUsername());
        }
    }

    @Override
    protected void doDelete(User user) {
        ZabbixUser zabbixUser = zabbixUserHandler.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) return;
        zabbixUserHandler.delete(configContext.get(), user.getUsername());
        // 清除缓存
        zabbixUserHandler.evictById(zabbixUser.getUserid());
        zabbixUserHandler.evictByUsername(user.getUsername());
        log.info("删除Zabbix用户: username = {}", user.getUsername());
    }

    @Override
    public void doGrant(User user, BaseBusiness.IBusiness businessResource) {
        ZabbixUser zabbixUser = zabbixUserHandler.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) {
            doCreate(user);
        } else {
            zabbixUserHandler.update(configContext.get(), zabbixUser, getUsrgrps(configContext.get(), user));
            // 清除缓存
            zabbixUserHandler.evictById(zabbixUser.getUserid());
            zabbixUserHandler.evictByUsername(user.getUsername());
            log.info("更新Zabbix用户: username = {}", user.getUsername());
        }
    }

    @Override
    public void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
        doGrant(user, businessResource);
    }

}
