package com.baiyi.opscloud.datasource.account.impl;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
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
    protected void doCreate(DsZabbixConfig.Zabbix zabbix, User user) {
        zabbixUserHandler.create(zabbix, AccountConvert.toZabbixUser(user), ZabbixMediaUtil.buildMedias(user), getUsrgrps(zabbix, user));
        log.info("创建Zabbix用户: url= {} , username = {}", zabbix.getUrl(), user.getUsername());
    }

    @Override
    protected void doUpdate(DsZabbixConfig.Zabbix zabbix, User user) {
        ZabbixUser zabbixUser = zabbixUserHandler.getByUsername(zabbix, user.getUsername());
        if (zabbixUser == null) {
            doCreate(zabbix, user);
        } else {
            ZabbixUser updateUser = AccountConvert.toZabbixUser(user);
            updateUser.setUserid(zabbixUser.getUserid());
            zabbixUserHandler.update(zabbix, updateUser, ZabbixMediaUtil.buildMedias(user), getUsrgrps(zabbix, user));
            // 清除缓存
            zabbixUserHandler.evictById(zabbixUser.getUserid());
            zabbixUserHandler.evictByUsername(user.getUsername());
            log.info("更新Zabbix用户: url= {} , username = {}", zabbix.getUrl(), user.getUsername());
        }
    }

    @Override
    protected void doDelete(DsZabbixConfig.Zabbix zabbix, User user) {
        ZabbixUser zabbixUser = zabbixUserHandler.getByUsername(zabbix, user.getUsername());
        if (zabbixUser == null) return;
        zabbixUserHandler.delete(zabbix, user.getUsername());
        // 清除缓存
        zabbixUserHandler.evictById(zabbixUser.getUserid());
        zabbixUserHandler.evictByUsername(user.getUsername());
        log.info("删除Zabbix用户: url= {} , username = {}", zabbix.getUrl(), user.getUsername());
    }

    @Override
    protected void doGrant(DsZabbixConfig.Zabbix zabbix, User user, BaseBusiness.IBusiness businessResource) {
        // Zabbix不处理用户授权用户组事件
    }

    @Override
    protected void doRevoke(DsZabbixConfig.Zabbix zabbix, User user, BaseBusiness.IBusiness businessResource) {
        // Zabbix不处理用户撤销用户组事件
    }

}
