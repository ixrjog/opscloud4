package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.aliyun.ram.drive.AliyunRamUserDrive;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserRamParam;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.facade.datasource.DsInstanceFacade;
import com.baiyi.opscloud.facade.user.UserRamFacade;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/12/12 4:10 PM
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserRamFacadeImpl implements UserRamFacade {

    private final AliyunRamUserDrive aliyunRamUserDrive;

    private final DsConfigHelper dsConfigHelper;

    private final UserService userService;

    private final StringEncryptor stringEncryptor;

    private final DsInstanceFacade dsInstanceFacade;

    private final static boolean DO_NOT_RESET = false;

    @Override
    public void createUser(UserRamParam.CreateRamUser createRamUser) {
        User user = userService.getByUsername(createRamUser.getUsername());
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(stringEncryptor.decrypt(user.getPassword()));
        }
        DatasourceConfig config = dsConfigHelper.getConfigByInstanceUuid(createRamUser.getInstanceUuid());
        AliyunConfig.Aliyun aliyun = dsConfigHelper.build(config, AliyunConfig.class).getAliyun();
        RamUser.User ramUser = aliyunRamUserDrive.createUser(aliyun.getRegionId(), aliyun, user, DO_NOT_RESET);
        // 同步资产
        dsInstanceFacade.pullAsset(createRamUser.getInstanceUuid(), DsAssetTypeEnum.RAM_USER.name(), ramUser);
    }

}
