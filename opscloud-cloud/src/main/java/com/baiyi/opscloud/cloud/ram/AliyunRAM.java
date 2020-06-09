package com.baiyi.opscloud.cloud.ram;

import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.aliyun.ram.handler.AliyunRAMHandler;
import com.baiyi.opscloud.cloud.ram.builder.AliyunRamUserBuilder;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 11:36 上午
 * @Version 1.0
 */
@Component
public class AliyunRAM {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunRAMHandler aliyunRAMHandler;

    public List<ListUsersResponse.User> getUsers(AliyunAccount aliyunAccount) {
        return aliyunRAMHandler.getUsers(aliyunAccount);
    }

    public BusinessWrapper<Boolean> syncUsers() {
        aliyunCore.getAccounts().forEach(e -> {
            List<ListUsersResponse.User> users = getUsers(e);
            syncUsers(e, users);
        });
        return BusinessWrapper.SUCCESS;
    }

    private void syncUsers(AliyunAccount aliyunAccount, List<ListUsersResponse.User> users) {
        if (users == null) return;
        users.forEach(e -> {
            OcAliyunRamUser ocAliyunRamUser = AliyunRamUserBuilder.build(aliyunAccount, e);

        });

    }


}
