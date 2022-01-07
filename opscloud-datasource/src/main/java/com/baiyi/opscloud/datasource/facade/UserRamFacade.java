package com.baiyi.opscloud.datasource.facade;

import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ram.entity.RamUser;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserRamParam;

/**
 * @Author baiyi
 * @Date 2021/12/12 4:10 PM
 * @Version 1.0
 */
public interface UserRamFacade {

    /**
     * 创建RAM账户
     *
     * @param createRamUser
     */
    void createRamUser(UserRamParam.CreateRamUser createRamUser);

    RamUser.User createUser(AliyunConfig.Aliyun aliyun, String instanceUuid, User user);

    void grantRamPolicy(UserRamParam.GrantRamPolicy grantRamPolicy);

    void revokeRamPolicy(UserRamParam.RevokeRamPolicy revokeRamPolicy);

}
