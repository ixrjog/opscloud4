package com.baiyi.opscloud.datasource.facade.am;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamPolicyDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamUserDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.notice.message.CreateRamUserMessage;
import com.baiyi.opscloud.domain.notice.message.UpdateRamLoginProfileMessage;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.param.user.UserAmParam;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamUserDriver.NO_PASSWORD_RESET_REQUIRED;

/**
 * Aliyun RAM
 *
 * @Author baiyi
 * @Date 2022/2/10 6:46 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ResourceAccessManagementProcessor extends AbstractAccessManagementProcessor {

    private final AliyunRamUserDriver aliyunRamUserDriver;

    private final AliyunRamPolicyDriver aliyunRamPolicyDriver;

    private final InstanceHelper instanceHelper;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final BusinessTagService bizTagService;

    @Override
    public final String getDsType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    protected void createUser(String instanceUuid, User user) {
        AliyunConfig.Aliyun config = buildConfig(instanceUuid);
        createUser(config, instanceUuid, user);
    }

    @Override
    public void grantPolicy(UserAmParam.GrantPolicy grantPolicy) {
        User user = userService.getByUsername(grantPolicy.getUsername());
        AliyunConfig.Aliyun aliyun = buildConfig(grantPolicy.getInstanceUuid());
        try {
            RamUser.User ramUser = aliyunRamUserDriver.getUser(aliyun.getRegionId(), aliyun, grantPolicy.getUsername());
            if (ramUser == null) {
                ramUser = createUser(aliyun, grantPolicy.getInstanceUuid(), user);
            }
            RamPolicy.Policy ramPolicy = BeanCopierUtil.copyProperties(grantPolicy.getPolicy(), RamPolicy.Policy.class);
            if (aliyunRamUserDriver.listUsersForPolicy(aliyun.getRegionId(), aliyun, ramPolicy.getPolicyType(), ramPolicy.getPolicyName()).stream().anyMatch(e -> e.getUserName().equals(grantPolicy.getUsername()))) {
                throw new OCException("重复授权！");
            }
            aliyunRamPolicyDriver.attachPolicyToUser(aliyun.getRegionId(), aliyun, ramUser.getUserName(), ramPolicy);
            RamPolicy.Policy policy = aliyunRamPolicyDriver.getPolicy(aliyun.getRegionId(), aliyun, ramPolicy);
            // 同步资产 RAM_USER
            dsInstanceFacade.pullAsset(grantPolicy.getInstanceUuid(), DsAssetTypeConstants.RAM_USER.name(), ramUser);
            postHandle(grantPolicy.getInstanceUuid(), policy);
        } catch (ClientException e) {
            if (e.getMessage().startsWith("EntityAlreadyExists.User.Policy")) {
                throw new OCException("重复授权！");
            }
            throw new OCException(e.getMessage());
        }
    }

    /**
     * 后处理，同步DMS用户
     *
     * @param instanceUuid
     */
    private void postHandle(String instanceUuid, RamPolicy.Policy policy) {
        DatasourceInstanceAsset query = DatasourceInstanceAsset.builder()
                .instanceUuid(instanceUuid)
                .assetId(policy.getPolicyName())
                .assetKey(policy.getPolicyType())
                .assetType(DsAssetTypeConstants.RAM_POLICY.name())
                .build();
        DatasourceInstanceAsset policyAsset = dsInstanceAssetService.getByUniqueKey(query);
        if (policyAsset == null) {
            return;
        }
        BusinessTag businessTag = BusinessTag.builder()
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(policyAsset.getId())
                .tagId(1)
                .build();
        // 没有DMS标签
        if (bizTagService.countByBusinessTag(businessTag) == 0) {
            return;
        }
        DatasourceInstance dsInstance = instanceHelper.getInstanceByUuid(instanceUuid);
        DsAssetParam.PushAsset pushAsset = DsAssetParam.PushAsset.builder().assetType(DsAssetTypeConstants.DMS_USER.name()).instanceId(dsInstance.getId()).build();
        // 异步执行
        dsInstanceFacade.pushAsset(pushAsset);
    }

    @Override
    public void revokePolicy(UserAmParam.RevokePolicy revokePolicy) {
        User user = userService.getByUsername(revokePolicy.getUsername());
        AliyunConfig.Aliyun aliyun = buildConfig(revokePolicy.getInstanceUuid());
        try {
            RamUser.User ramUser = aliyunRamUserDriver.getUser(aliyun.getRegionId(), aliyun, revokePolicy.getUsername());
            //   throw new CommonRuntimeException("RAM用户撤销授权策略错误：RAM账户不存在！");
            if (ramUser != null) {
                RamPolicy.Policy ramPolicy = BeanCopierUtil.copyProperties(revokePolicy.getPolicy(), RamPolicy.Policy.class);
                //    throw new CommonRuntimeException("RAM用户撤销授权策略错误： 未授权当前策略！");
                if (aliyunRamUserDriver.listUsersForPolicy(aliyun.getRegionId(), aliyun, ramPolicy.getPolicyType(), ramPolicy.getPolicyName()).stream().anyMatch(e -> e.getUserName().equals(revokePolicy.getUsername()))) {
                    aliyunRamPolicyDriver.detachPolicyFromUser(aliyun.getRegionId(), aliyun, ramUser.getUserName(), ramPolicy);
                    RamPolicy.Policy policy = aliyunRamPolicyDriver.getPolicy(aliyun.getRegionId(), aliyun, ramPolicy);
                }
                // 同步资产 RAM_USER
                dsInstanceFacade.pullAsset(revokePolicy.getInstanceUuid(), DsAssetTypeConstants.RAM_USER.name(), ramUser);
            }
        } catch (ClientException e) {
            throw new OCException("阿里云接口查询错误: {}", e.getMessage());
        }

    }

    public RamUser.User createUser(AliyunConfig.Aliyun aliyun, String instanceUuid, User user) {
        RamUser.User ramUser;
        try {
            ramUser = aliyunRamUserDriver.getUser(aliyun.getRegionId(), aliyun, user.getUsername());
            if (ramUser != null) {
                return ramUser;
            }
        } catch (ClientException ignore) {
        }
        ramUser = aliyunRamUserDriver.createUser(aliyun.getRegionId(), aliyun, user, CREATE_LOGIN_PROFILE, enableMFA(instanceUuid));
        // 同步资产 RAM_USER
        dsInstanceFacade.pullAsset(instanceUuid, DsAssetTypeConstants.RAM_USER.name(), ramUser);
        CreateRamUserMessage message = CreateRamUserMessage.builder()
                .aliyunName(aliyun.getAccount().getName())
                .loginUrl(aliyun.getAccount().getLoginUrl(aliyun.getVersion()))
                .username(Joiner.on("").join(ramUser.getUserName(), aliyun.getAccount().getDomain()))
                .password(user.getPassword())
                .build();
        noticeManager.sendMessage(user, NoticeManager.MsgKeys.CREATE_RAM_USER, message);
        return ramUser;
    }

    private AliyunConfig.Aliyun buildConfig(String instanceUuid) {
        DatasourceConfig config = dsConfigManager.getConfigByInstanceUuid(instanceUuid);
        return dsConfigManager.build(config, AliyunConfig.class).getAliyun();
    }

    @Override
    public void updateLoginProfile(UserAmParam.UpdateLoginProfile updateLoginProfile) {
        AliyunConfig.Aliyun config = buildConfig(updateLoginProfile.getInstanceUuid());
        User user = userService.getByUsername(updateLoginProfile.getUsername());
        try {
            aliyunRamUserDriver.updateLoginProfile(config, user, updateLoginProfile.getPassword(), NO_PASSWORD_RESET_REQUIRED);
            UpdateRamLoginProfileMessage message = UpdateRamLoginProfileMessage.builder()
                    .aliyunName(config.getAccount().getName())
                    .loginUrl(config.getAccount().getLoginUrl(config.getVersion()))
                    .username(updateLoginProfile.getUsername())
                    .password(updateLoginProfile.getPassword())
                    .build();
            noticeManager.sendMessage(user, NoticeManager.MsgKeys.ALIYUN_RAM_UPDATE_LOGIN_PROFILE, message);
        } catch (ClientException e) {
            throw new OCException("更新RAM用户登录配置错误: {}", e.getMessage());
        }
    }

}
