package com.baiyi.opscloud.cloud.ram;

import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 11:36 上午
 * @Version 1.0
 */

public interface AliyunRAMUserCenter {

    BusinessWrapper<OcAliyunRamUser> createRamUser(String accountUid, OcUser ocUser);

    BusinessWrapper<Boolean> deleteRamUser(OcAliyunRamUser ocAliyunRamUser);

    List<ListUsersResponse.User> getUsers(AliyunCoreConfig.AliyunAccount aliyunAccount);

    BusinessWrapper<Boolean> syncUsers();

    BusinessWrapper<Boolean> syncUser(OcAliyunRamUser ocAliyunRamUser);

}
