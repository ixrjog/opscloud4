package com.baiyi.opscloud.datasource.account.impl;

import com.baiyi.opscloud.datasource.account.convert.AccountConvert;
import com.baiyi.opscloud.datasource.account.impl.base.BaseZabbixAccountProvider;
import com.baiyi.opscloud.datasource.account.util.ZabbixMediaUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5UserDrive;
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
    private ZabbixV5UserDrive zabbixV5UserDatasource;

    public static final String ZABBIX_DEFAULT_USERGROUP = "users_default";

    private void postCacheEvict(com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User zabbixUser, User user) {
        zabbixV5UserDatasource.evictById(configContext.get(), zabbixUser.getUserid());
        zabbixV5UserDatasource.evictByUsername(configContext.get(), user.getUsername());
    }

    @Override
    protected void doCreate(User user) {
        zabbixV5UserDatasource.create(configContext.get(), AccountConvert.toZabbixUser(user), ZabbixMediaUtil.buildMedias(user), getUsrgrps(configContext.get(), user));
        log.info("创建Zabbix用户: username = {}", user.getUsername());
    }

    @Override
    protected void doUpdate(User user) {
       com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User zabbixUser = zabbixV5UserDatasource.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) {
            doCreate(user);
        } else {
            com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User updateUser = AccountConvert.toZabbixUser(user);
            updateUser.setUserid(zabbixUser.getUserid());
            zabbixV5UserDatasource.update(configContext.get(), updateUser, getUsrgrps(configContext.get(), user), ZabbixMediaUtil.buildMedias(user));
            // 清除缓存
            postCacheEvict(zabbixUser,user);
            log.info("更新Zabbix用户: username = {}", user.getUsername());
        }
    }

    @Override
    protected void doDelete(User user) {
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User zabbixUser = zabbixV5UserDatasource.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) return;
        zabbixV5UserDatasource.delete(configContext.get(), user.getUsername());
        // 清除缓存
        postCacheEvict(zabbixUser,user);
        log.info("删除Zabbix用户: username = {}", user.getUsername());
    }

    @Override
    public void doGrant(User user, BaseBusiness.IBusiness businessResource) {
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser.User zabbixUser = zabbixV5UserDatasource.getByUsername(configContext.get(), user.getUsername());
        if (zabbixUser == null) {
            doCreate(user);
        } else {
            zabbixV5UserDatasource.update(configContext.get(), zabbixUser, getUsrgrps(configContext.get(), user));
            // 清除缓存
            postCacheEvict(zabbixUser,user);
            log.info("更新Zabbix用户: username = {}", user.getUsername());
        }
    }

    @Override
    public void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
        doGrant(user, businessResource);
    }

}
