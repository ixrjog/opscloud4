package com.sdg.cmdb.service.impl;


import com.aliyun.openservices.log.common.MachineGroup;

import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.logService.logServiceQuery.LogServiceServerGroupCfgVO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AliyunLogManageServiceImplTest {

    @Resource
    private AliyunLogManageServiceImpl aliyunLogManageServiceImpl;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Test
    public void testQuerListProjects() {
        List<String> projects = aliyunLogManageServiceImpl.queryListProject();
        System.err.println("Projects:" + projects.toString() + "\n");
    }


    @Test
    public void testQuerListLogStores() {
        List<String> logStores = aliyunLogManageServiceImpl.queryListLogStores("collect-web-service-logs");
        System.err.println("ListLogs:" + logStores.toString() + "\n");
    }

    @Test
    public void testQuerListConfigs() {
        List<String> configs = aliyunLogManageServiceImpl.queryListConfig("apps-logs","gateway");
        System.err.println("configs :" + configs.toString() + "\n");
    }


    @Test
    public void testQueryListLogs() {
        List<String> list = aliyunLogManageServiceImpl.queryListMachineGroup("collect-web-service-logs","");
        System.err.println("MachineGroups:" + list.toString() + "\n");
    }


    @Test
    public void testGetMachineGroup() {
       MachineGroup mg = aliyunLogManageServiceImpl.getMachineGroup("collect-web-service-logs","group_trade");
        System.err.println("MachineGroup:" +  mg.toString() + "\n");
    }



}
