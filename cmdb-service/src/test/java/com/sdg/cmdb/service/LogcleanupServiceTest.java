package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.service.impl.LogcleanupServiceImpl;
import com.sdg.cmdb.service.impl.ServerServiceImpl;
import com.sdg.cmdb.util.ByteUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;



/**
 * Created by liangjian on 2017/3/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class LogcleanupServiceTest {

    @Autowired
    private LogcleanupServiceImpl logcleanupService;

    @Resource
    private ServerService serverService;

    @Test
    public void test1() {
        logcleanupService.updateLogcleanupServers();
    }

    @Test
    public void test2() {
       System.err.println(ByteUtils.getSize(105553100800l));
    }


    @Test
    public void test3() {
       // ServerDO serverDO = serverService.getServerById(493);
        logcleanupService.updateLogcleanupConfig(493);
    }

    @Test
    public void test4() {
        // ServerDO serverDO = serverService.getServerById(493);
        logcleanupService.updateLogcleanupServers();
    }

}
