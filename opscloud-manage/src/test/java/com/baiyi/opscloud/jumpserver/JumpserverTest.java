package com.baiyi.opscloud.jumpserver;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
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
    void test2(){
        // jumpserverAPI.pushKey();
      // System.err.println(jumpserverAPI.getToken());
    }


}
