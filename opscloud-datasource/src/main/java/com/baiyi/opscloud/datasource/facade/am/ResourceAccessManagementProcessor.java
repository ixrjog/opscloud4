package com.baiyi.opscloud.datasource.facade.am;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamPolicyDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.driver.AliyunRamUserDriver;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.notice.message.CreateRamUserMessage;
import com.baiyi.opscloud.domain.param.user.UserAmParam;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/10 6:46 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ResourceAccessManagementProcessor extends AbstractAccessManagementProcessor {

    private final AliyunRamUserDriver aliyunRamUserDriver;

    private final AliyunRamPolicyDriver aliyunRamPolicyDriver;

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
            if (ramUser == null)
                ramUser = createUser(aliyun, grantPolicy.getInstanceUuid(), user);
            RamPolicy.Policy ramPolicy = BeanCopierUtil.copyProperties(grantPolicy.getPolicy(), RamPolicy.Policy.class);
            if (aliyunRamUserDriver.listUsersForPolicy(aliyun.getRegionId(), aliyun, ramPolicy.getPolicyType(), ramPolicy.getPolicyName())
                    .stream().anyMatch(e -> e.getUserName().equals(grantPolicy.getUsername())))
                throw new CommonRuntimeException("RAM用户授权策略错误: 重复授权！");
            aliyunRamPolicyDriver.attachPolicyToUser(aliyun.getRegionId(), aliyun, ramUser.getUserName(), ramPolicy);
            RamPolicy.Policy policy = aliyunRamPolicyDriver.getPolicy(aliyun.getRegionId(), aliyun, ramPolicy);
            // 同步资产 RAM_USER
            dsInstanceFacade.pullAsset(grantPolicy.getInstanceUuid(), DsAssetTypeConstants.RAM_USER.name(), ramUser);
        } catch (ClientException e) {
            throw new CommonRuntimeException("阿里云接口查询错误！");
        }
    }

    @Override
    public void revokePolicy(UserAmParam.RevokePolicy revokePolicy) {
        User user = userService.getByUsername(revokePolicy.getUsername());
        AliyunConfig.Aliyun aliyun = buildConfig(revokePolicy.getInstanceUuid());
        try {
            RamUser.User ramUser = aliyunRamUserDriver.getUser(aliyun.getRegionId(), aliyun, revokePolicy.getUsername());
            //    throw new CommonRuntimeException("RAM用户撤销授权策略错误：RAM账户不存在！");
            if (ramUser != null) {
                RamPolicy.Policy ramPolicy = BeanCopierUtil.copyProperties(revokePolicy.getPolicy(), RamPolicy.Policy.class);
                //    throw new CommonRuntimeException("RAM用户撤销授权策略错误： 未授权当前策略！");
                if (aliyunRamUserDriver.listUsersForPolicy(aliyun.getRegionId(), aliyun, ramPolicy.getPolicyType(), ramPolicy.getPolicyName())
                        .stream().anyMatch(e -> e.getUserName().equals(revokePolicy.getUsername()))) {
                    aliyunRamPolicyDriver.detachPolicyFromUser(aliyun.getRegionId(), aliyun, ramUser.getUserName(), ramPolicy);
                    RamPolicy.Policy policy = aliyunRamPolicyDriver.getPolicy(aliyun.getRegionId(), aliyun, ramPolicy);
                }
                // 同步资产 RAM_USER
                dsInstanceFacade.pullAsset(revokePolicy.getInstanceUuid(), DsAssetTypeConstants.RAM_USER.name(), ramUser);
            }
        } catch (ClientException e) {
            throw new CommonRuntimeException("阿里云接口查询错误！");
        }

    }

    public RamUser.User createUser(AliyunConfig.Aliyun aliyun, String instanceUuid, User user) {
        RamUser.User ramUser;
        try {
            ramUser = aliyunRamUserDriver.getUser(aliyun.getRegionId(), aliyun, user.getUsername());
            if (ramUser != null)
                return ramUser;
        } catch (ClientException ignore) {
        }
        ramUser = aliyunRamUserDriver.createUser(aliyun.getRegionId(), aliyun, user, CREATE_LOGIN_PROFILE, enableMFA(instanceUuid));
        // 同步资产 RAM_USER
        dsInstanceFacade.pullAsset(instanceUuid, DsAssetTypeConstants.RAM_USER.name(), ramUser);
        CreateRamUserMessage message = CreateRamUserMessage.builder()
                .aliyunName(aliyun.getAccount().getName())
                .loginUrl(aliyun.getAccount().getLoginUrl())
                .username(Joiner.on("").join(ramUser.getUserName(), aliyun.getAccount().getDomain()))
                .password(user.getPassword())
                .build();
        noticeManager.sendMessage(user, NoticeManager.MsgKeys.CREATE_RAM_USER, message);
        return ramUser;
    }

    private AliyunConfig.Aliyun buildConfig(String instanceUuid) {
        DatasourceConfig config = dsConfigHelper.getConfigByInstanceUuid(instanceUuid);
        return dsConfigHelper.build(config, AliyunConfig.class).getAliyun();
    }

}