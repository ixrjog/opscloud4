package com.sdg.cmdb.service;


import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ServerServiceTest {


    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Test
    public void test() {
       ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_zebra");
       List<ServerDO> serverList =serverDao.getServersByGroupIdAndEnvType(serverGroupDO.getId(),4);


        List<ServerDO> removeServerList = new ArrayList<>();
        removeServerList.add(serverDao.getServerInfoById(12));
        removeServerList.add(serverDao.getServerInfoById(13));

        boolean b =serverList.removeAll(removeServerList);

        serverList.remove(serverDao.getServerInfoById(12));
        System.err.println(b);
        for(ServerDO serverDO:serverList){
            System.err.println(serverDO);
        }

    }


    @Test
    public void test2() {
       int n= serverDao.getMyServerSize("baiyi");
       System.err.println(n);

    }

}
