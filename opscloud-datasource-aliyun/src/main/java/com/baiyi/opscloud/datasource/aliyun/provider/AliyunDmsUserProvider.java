package com.baiyi.opscloud.datasource.aliyun.provider;

import com.baiyi.opscloud.common.annotation.SingleTask;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.comparer.AssetComparer;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.asset.BaseAssetProvider;
import com.baiyi.opscloud.datasource.aliyun.dms.driver.AliyunDmsTenantDriver;
import com.baiyi.opscloud.datasource.aliyun.dms.driver.AliyunDmsUserDriver;
import com.baiyi.opscloud.datasource.aliyun.dms.entity.DmsUser;
import com.baiyi.opscloud.datasource.aliyun.provider.push.AliyunDmsUserPushHelper;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PULL_ALIYUN_DMS_USER;
import static com.baiyi.opscloud.common.constants.SingleTaskConstants.PUSH_ALIYUN_DMS_USER;

/**
 * @Author baiyi
 * @Date 2021/12/16 4:19 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunDmsUserProvider extends BaseAssetProvider<DmsUser.User> {

    @Resource
    private AliyunDmsUserProvider aliyunDmsUserProvider;

    @Resource
    private AliyunDmsUserPushHelper aliyunDmsUserPushHelper;

    @Override
    @SingleTask(name = PULL_ALIYUN_DMS_USER, lockTime = "2m")
    public void pullAsset(int dsInstanceId) {
        doPull(dsInstanceId);
    }
    /**
     * 同步资产
     *
     * @param dsInstanceId
     */
    @Override
    @SingleTask(name = PUSH_ALIYUN_DMS_USER, lockTime = "2m")
    public void pushAsset(int dsInstanceId) {
        DsInstanceContext dsInstanceContext = buildDsInstanceContext(dsInstanceId);
        List<DmsUser.User> users = aliyunDmsUserPushHelper.getPushAssets(dsInstanceContext);
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        try {
            Long tid = Optional.of(aliyun)
                    .map(AliyunConfig.Aliyun::getDms)
                    .map(AliyunConfig.Dms::getTid)
                    .orElse(AliyunDmsTenantDriver.getTenant(aliyun).getTid());
            users.forEach(dmsUser -> {
                try {
                    AliyunDmsUserDriver.registerUser(aliyun, tid, dmsUser);
                } catch (Exception e) {
                    log.error("注册用户错误: nickName={}, {}", dmsUser.nickName, e.getMessage());
                }
            });
            this.doPull(dsInstanceId);
        } catch (Exception e) {
            log.error("推送DMS用户资产错误: 查询租户TID失败！");
        }
    }

    private AliyunConfig.Aliyun buildConfig(DatasourceConfig dsConfig) {
        return dsConfigManager.build(dsConfig, AliyunConfig.class).getAliyun();
    }

    @Override
    protected AssetComparer getAssetComparer() {
        return AssetComparer.COMPARE_NAME;
    }

    @Override
    protected List<DmsUser.User> listEntities(DsInstanceContext dsInstanceContext) {
        AliyunConfig.Aliyun aliyun = buildConfig(dsInstanceContext.getDsConfig());
        try {
            Long tid = Optional.of(aliyun)
                    .map(AliyunConfig.Aliyun::getDms)
                    .map(AliyunConfig.Dms::getTid)
                    .orElse(AliyunDmsTenantDriver.getTenant(aliyun).getTid());
            return AliyunDmsUserDriver.listUser(aliyun, tid);
        } catch (Exception e) {
            log.error("获取条目错误: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.DMS_USER.name();
    }

    @Override
    public void afterPropertiesSet() {
        AssetProviderFactory.register(aliyunDmsUserProvider);
    }

}