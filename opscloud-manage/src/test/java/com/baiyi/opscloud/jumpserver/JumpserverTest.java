package com.baiyi.opscloud.jumpserver;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.jumpserver.api.JumpserverAPI;
import com.baiyi.opscloud.service.jumpserver.UsersUserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/8 8:29 下午
 * @Version 1.0
 */
public class JumpserverTest extends BaseUnit {

    @Resource
    private UsersUserService usersUserService;

    @Resource
    private JumpserverAPI jumpserverAPI;

    @Test
    void test(){
      UsersUser usersUser = usersUserService.queryUsersUserByUsername("baiyi");
      System.err.println(JSON.toJSONString(usersUser));
    }

    @Test
    void tes5(){
        System.err.println(UUIDUtils.getUUID());
    }

    @Test
    void test3(){
        UsersUser usersUser = usersUserService.queryUsersUserByUsername("koko-1");

        usersUser.setId(UUIDUtils.getUUID());
        usersUser.setUsername("koko-2");
        usersUser.setName("koko-2");
        usersUser.setEmail("jumpserver-new-4@serviceaccount.local");
        //8436ae4b7dc24327bdb08f2ec7d09c6a

        System.err.println(JSON.toJSONString(usersUser));
        usersUserService.addUsersUser(usersUser);
    }




}
