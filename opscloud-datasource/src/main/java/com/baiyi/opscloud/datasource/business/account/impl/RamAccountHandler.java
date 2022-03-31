package com.baiyi.opscloud.datasource.business.account.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamAccessKeyDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamUserDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.AccessKey;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.datasource.business.account.impl.base.AbstractAccountHandler;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/3/31 09:35
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RamAccountHandler extends AbstractAccountHandler {

    private final AliyunRamUserDriver aliyunRamUserDriver;

    private final AliyunRamAccessKeyDriver aliyunRamAccessKeyDriver;

    private final DsInstanceAssetService assetService;

    protected static ThreadLocal<AliyunConfig.Aliyun> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigHelper.build(dsConfig, AliyunConfig.class).getAliyun());
    }

    @Override
    protected void doCreate(User user) {
        // Not Supported
    }

    @Override
    protected void doUpdate(User user) {
        // TODO 从资产表查询用户，对比用户信息后再通过 API 更新 RAM 信息
    }

    @Override
    protected void doDelete(User user) {
        AliyunConfig.Aliyun aliyun = configContext.get();
        try {
            RamUser.User ramUser = aliyunRamUserDriver.getUser(aliyun.getRegionId(), aliyun, user.getUsername());
            if (ramUser == null) return;
            Optional<AccessKey.Key> optionalKey = aliyunRamAccessKeyDriver.listAccessKeys(aliyun.getRegionId(), aliyun, user.getUsername()).stream()
                    .filter(k -> "Active".equals(k.getStatus())).findFirst();
            if (optionalKey.isPresent()) {
                // 用户有(有效的)AK，禁止 Web 控制台登录
                aliyunRamUserDriver.deleteLoginProfile(aliyun.getRegionId(), aliyun, user.getUsername());
            } else {
                // 用户无AK，直接删除账户
                aliyunRamUserDriver.deleteUser(aliyun.getRegionId(), aliyun, user.getUsername());
            }
        } catch (ClientException e) {
            log.info("删除 RAM 用户错误: username = {} , message = {}", user.getUsername(), e.getMessage());
        }
    }

    @Override
    public void doGrant(User user, BaseBusiness.IBusiness businessResource) {
        // Not Supported
    }

    @Override
    public void doRevoke(User user, BaseBusiness.IBusiness businessResource) {
        // Not Supported
    }

    @Override
    protected int getBusinessResourceType() {
        return BusinessTypeEnum.COMMON.getType();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.getName();
    }

}
