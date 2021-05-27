package com.baiyi.caesar.facade.server.impl;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.facade.server.ServerGroupFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/25 9:15 上午
 * @Version 1.0
 */
class ServerGroupFacadeImplTest extends BaseUnit {

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Test
    void ddd(){
        serverGroupFacade.deleteServerGroupById(1);
    }

}