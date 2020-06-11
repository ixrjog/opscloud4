package com.baiyi.opscloud.cloud.ram;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/9 11:33 上午
 * @Version 1.0
 */
public class AliyunRAMTest extends BaseUnit {

    @Resource
    private AliyunRAMUserCenter aliyunRAMUserCenter;

    @Resource
    private AliyunRAMPolicyCenter aliyunRAMPolicyCenter;

    @Resource
    private AliyunCore aliyunCore;

    @Test
    void testGetUsers() {
        aliyunCore.getAccounts().forEach(e -> {
            List<ListUsersResponse.User> users = aliyunRAMUserCenter.getUsers(e);
            System.err.println(JSON.toJSONString(users));
        });
    }

    @Test
    void testGetPolicies() {
        aliyunCore.getAccounts().forEach(e -> {
            List<ListPoliciesResponse.Policy> policies = aliyunRAMPolicyCenter.getPolicies(e);
            System.err.println(JSON.toJSONString(policies));
        });
    }

    @Test
    void testSyncUsers() {
        aliyunRAMUserCenter.syncUsers();
    }

    @Test
    void testSyncPolicies() {
        aliyunRAMPolicyCenter.syncPolicies();
    }
}
