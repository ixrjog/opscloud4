package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.facade.user.UserFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author baiyi
 * @Date 2021/9/15 3:30 下午
 * @Version 1.0
 */
class UserFacadeImplTest extends BaseUnit {

    @Resource
    private UserFacade userFacade;

    @Test
    void dddTest(){
        userFacade.syncUsers();
    }

}