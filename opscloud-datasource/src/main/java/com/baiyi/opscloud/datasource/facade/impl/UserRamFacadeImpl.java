package com.baiyi.opscloud.datasource.facade.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.aliyun.ram.drive.AliyunRamPolicyDrive;
import com.baiyi.opscloud.datasource.aliyun.ram.drive.AliyunRamUserDrive;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamPolicy;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.datasource.facade.UserRamFacade;
import com.baiyi.opscloud.datasource.manager.base.NoticeManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.notice.message.CreateRamUserMessage;
import com.baiyi.opscloud.domain.param.user.UserRamParam;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/12/12 4:10 PM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserRamFacadeImpl implements UserRamFacade {

    private final AliyunRamUserDrive aliyunRamUserDrive;

    private final AliyunRamPolicyDrive aliyunRamPolicyDrive;

    private final DsConfigHelper dsConfigHelper;

    private final UserService userService;

    private final StringEncryptor stringEncryptor;

    private final DsInstanceFacade dsInstanceFacade;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final NoticeManager noticeManager;

    public final static boolean CREATE_LOGIN_PROFILE = true;

    @Override
    public void createRamUser(UserRamParam.CreateRamUser createRamUser) {
        User user = userService.getByUsername(createRamUser.getUsername());
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(stringEncryptor.decrypt(user.getPassword()));
        }
        DatasourceConfig config = dsConfigHelper.getConfigByInstanceUuid(createRamUser.getInstanceUuid());
        AliyunConfig.Aliyun aliyun = dsConfigHelper.build(config, AliyunConfig.class).getAliyun();
        createUser(aliyun, createRamUser.getInstanceUuid(), user);
    }

    @Override
    public RamUser.User createUser(AliyunConfig.Aliyun aliyun, String instanceUuid, User user) {
        RamUser.User ramUser;
        try {
            ramUser = aliyunRamUserDrive.getUser(aliyun.getRegionId(), aliyun, user.getUsername());
            if (ramUser != null)
                return ramUser;
        } catch (ClientException ignore) {
        }
        ramUser = aliyunRamUserDrive.createUser(aliyun.getRegionId(), aliyun, user, CREATE_LOGIN_PROFILE);
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

    @Override
    public void grantRamPolicy(UserRamParam.GrantRamPolicy grantRamPolicy) {
        User user = userService.getByUsername(grantRamPolicy.getUsername());
        DatasourceConfig config = dsConfigHelper.getConfigByInstanceUuid(grantRamPolicy.getInstanceUuid());
        AliyunConfig.Aliyun aliyun = dsConfigHelper.build(config, AliyunConfig.class).getAliyun();
        try {
            RamUser.User ramUser = aliyunRamUserDrive.getUser(aliyun.getRegionId(), aliyun, grantRamPolicy.getUsername());
            if (ramUser == null)
                ramUser = createUser(aliyun, grantRamPolicy.getInstanceUuid(), user);
            RamPolicy.Policy ramPolicy = BeanCopierUtil.copyProperties(grantRamPolicy.getPolicy(), RamPolicy.Policy.class);
            if (aliyunRamUserDrive.listUsersForPolicy(aliyun.getRegionId(), aliyun, ramPolicy.getPolicyType(), ramPolicy.getPolicyName())
                    .stream().anyMatch(e -> e.getUserName().equals(grantRamPolicy.getUsername())))
                throw new CommonRuntimeException("RAM用户授权策略错误： 重复授权！");
            aliyunRamPolicyDrive.attachPolicyToUser(aliyun.getRegionId(), aliyun, ramUser.getUserName(), ramPolicy);
            RamPolicy.Policy policy = aliyunRamPolicyDrive.getPolicy(aliyun.getRegionId(), aliyun, ramPolicy);
            // 同步资产 RAM_USER
            dsInstanceFacade.pullAsset(grantRamPolicy.getInstanceUuid(), DsAssetTypeConstants.RAM_USER.name(), ramUser);
        } catch (ClientException e) {
            throw new CommonRuntimeException("AliyunAPI查询错误！");
        }
    }

    public void revokeRamPolicy(UserRamParam.RevokeRamPolicy revokeRamPolicy) {
        User user = userService.getByUsername(revokeRamPolicy.getUsername());
        DatasourceConfig config = dsConfigHelper.getConfigByInstanceUuid(revokeRamPolicy.getInstanceUuid());
        AliyunConfig.Aliyun aliyun = dsConfigHelper.build(config, AliyunConfig.class).getAliyun();
        try {
            RamUser.User ramUser = aliyunRamUserDrive.getUser(aliyun.getRegionId(), aliyun, revokeRamPolicy.getUsername());
            //    throw new CommonRuntimeException("RAM用户撤销授权策略错误：RAM账户不存在！");
            if (ramUser != null) {
                RamPolicy.Policy ramPolicy = BeanCopierUtil.copyProperties(revokeRamPolicy.getPolicy(), RamPolicy.Policy.class);
                //    throw new CommonRuntimeException("RAM用户撤销授权策略错误： 未授权当前策略！");
                if (aliyunRamUserDrive.listUsersForPolicy(aliyun.getRegionId(), aliyun, ramPolicy.getPolicyType(), ramPolicy.getPolicyName())
                        .stream().anyMatch(e -> e.getUserName().equals(revokeRamPolicy.getUsername()))) {
                    aliyunRamPolicyDrive.detachPolicyFromUser(aliyun.getRegionId(), aliyun, ramUser.getUserName(), ramPolicy);
                    RamPolicy.Policy policy = aliyunRamPolicyDrive.getPolicy(aliyun.getRegionId(), aliyun, ramPolicy);
                }
                // 同步资产 RAM_USER
                dsInstanceFacade.pullAsset(revokeRamPolicy.getInstanceUuid(), DsAssetTypeConstants.RAM_USER.name(), ramUser);
            }
        } catch (ClientException e) {
            throw new CommonRuntimeException("AliyunAPI查询错误！");
        }
    }

}
