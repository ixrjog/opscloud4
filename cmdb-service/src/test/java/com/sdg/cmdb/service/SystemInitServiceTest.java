package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserLeaveDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.service.control.configurationfile.SystemInitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/7/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class SystemInitServiceTest {


    @Resource
    private SystemInitService systemInitService;

    @Test
    public void testAddUserLeave2() {
        ServerDO serverDO = new ServerDO();
        serverDO.setServerName("hello");
        serverDO.setInsideIp("10.17.1.1");
        System.err.println(systemInitService.acqSystemInitHostConfig(serverDO));
    }

}
