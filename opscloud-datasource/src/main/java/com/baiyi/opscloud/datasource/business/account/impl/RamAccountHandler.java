package com.baiyi.opscloud.datasource.business.account.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamUserDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.datasource.business.account.impl.base.AbstractAccountHandler;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    private final DsInstanceAssetService assetService;

    protected static ThreadLocal<AliyunConfig.Aliyun> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun());
    }

    @Override
    protected void doCreate(User user) {
        // Not Supported
    }

    /**
     * 从资产表查询用户，对比用户信息后再通过 API 更新 RAM 信息
     *
     * @param user
     */
    @Override
    protected void doUpdate(User user) {
        DatasourceInstance instance = dsInstanceContext.get().getDsInstance();
        DatasourceInstanceAsset query = DatasourceInstanceAsset.builder()
                .assetKey(user.getUsername())
                .assetType(DsAssetTypeConstants.RAM_USER.name())
                .instanceUuid(instance.getUuid())
                .build();
        List<DatasourceInstanceAsset> ramAssets = assetService.queryAssetByAssetParam(query);
        if (CollectionUtils.isEmpty(ramAssets)) {
            return;
        }
        AliyunConfig.Aliyun aliyun = configContext.get();
        ramAssets.forEach(ramAsset -> {
            try {
                RamUser.User ramUser = aliyunRamUserDriver.getUser(aliyun.getRegionId(), aliyun, user.getUsername());
                if (ramUser == null) {
                    return;
                }
                RamUser.User preRamUser = RamUser.User.builder()
                        .userName(user.getUsername())
                        .displayName(user.getDisplayName().equals(ramUser.getDisplayName()) ? null : user.getDisplayName())
                        .email(!StringUtils.isEmpty(user.getEmail()) && user.getEmail().equals(ramUser.getEmail()) ? null : user.getEmail())
                        .build();
                // 需要更新用户信息
                if (preRamUser.needUpdate()) {
                    aliyunRamUserDriver.updateUser(aliyun.getRegionId(), aliyun, preRamUser);
                }
            } catch (Exception e) {
                log.error("更新RAM信息错误: username={}, {}", user.getUsername(), e.getMessage());
            }
        });
    }

    @Override
    protected void doDelete(User user) {
        AliyunConfig.Aliyun aliyun = configContext.get();
        try {
            RamUser.User ramUser = aliyunRamUserDriver.getUser(aliyun.getRegionId(), aliyun, user.getUsername());
            if (ramUser == null) {
                return;
            }
//            Optional<AccessKey.Key> optionalKey = aliyunRamAccessKeyDriver.listAccessKeys(aliyun.getRegionId(), aliyun, user.getUsername()).stream()
//                    .filter(k -> "Active".equals(k.getStatus())).findFirst();
//            if (optionalKey.isPresent()) {
//                // 用户有(有效的)AK，禁止 Web 控制台登录
//                aliyunRamUserDriver.deleteLoginProfile(aliyun.getRegionId(), aliyun, user.getUsername());
//            } else {
//                // 用户无AK，直接删除账户
//                aliyunRamUserDriver.deleteUser(aliyun.getRegionId(), aliyun, user.getUsername());
//            }
            aliyunRamUserDriver.deleteLoginProfile(aliyun.getRegionId(), aliyun, user.getUsername());
        } catch (ClientException e) {
            log.error("删除RAM用户错误: username={}, {}", user.getUsername(), e.getMessage());
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
