package com.baiyi.opscloud.cloud.ram;

import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.domain.BusinessWrapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 11:36 上午
 * @Version 1.0
 */

public interface AliyunRAMUserCenter {

    List<ListUsersResponse.User> getUsers(AliyunAccount aliyunAccount);

    BusinessWrapper<Boolean> syncUsers();

}
