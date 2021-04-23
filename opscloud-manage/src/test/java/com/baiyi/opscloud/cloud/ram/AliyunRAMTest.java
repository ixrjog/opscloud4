package com.baiyi.opscloud.cloud.ram;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunRamUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.service.aliyun.ram.OcAliyunRamUserService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.jasypt.encryption.StringEncryptor;
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
    private OcAliyunRamUserService ocAliyunRamUserService;

    @Resource
    private AliyunRAMPolicyCenter aliyunRAMPolicyCenter;

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private StringEncryptor stringEncryptor;

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

    @Test
    void testDeleteRamUser() {
        // modao
        OcAliyunRamUser ocAliyunRamUser = ocAliyunRamUserService.queryOcAliyunRamUserByUniqueKey("1267986359450069", "ouyanggw");
        aliyunRAMUserCenter.deleteRamUser(ocAliyunRamUser);
    }


    @Test
    void testCreateRamUser() {
        // "1267986359450069"  "1255805305757185"
        // modao
        OcUser ocUser = ocUserService.queryOcUserByUsername("zhangyiwen");
        String passwd = "";
        ocUser.setPassword(passwd);
        System.err.println(passwd);
        BusinessWrapper<OcAliyunRamUser> wrapper = aliyunRAMUserCenter.createRamUser("1255805305757185", ocUser);
        System.err.println(JSON.toJSONString(wrapper));
    }

}
