package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class GatewayAdminServiceImplTest {

    @Autowired
    private GatewayAdminServiceImpl gatewayAdminService;

    @Autowired
    private ServerGroupDao serverGroupDao;

    @Test
    public void test1() {
        String r = gatewayAdminService.appServerList(19, EnvType.EnvTypeEnum.test.getCode());
        System.err.println(r);
    }

    @Test
    public void test2() {
        gatewayAdminService.syncGatewayAdmin();
    }
}
