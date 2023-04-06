package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.facade.server.ServerFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/5/9 11:13
 * @Version 1.0
 */
class ServerFacadeImplTest extends BaseUnit {

    @Resource
    private ServerFacade serverFacade;

    @Test
    void scanServerMonitoringStatusTest(){
        serverFacade.scanServerMonitoringStatus();
    }

}