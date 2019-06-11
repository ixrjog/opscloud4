package com.sdg.cmdb.service;


import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class NginxServiceTest {

    @Autowired
    private NginxService nginxService;

    @Autowired
    private ServerGroupService serverGroupService;

    @Autowired
    private ServerGroupDao serverGroupDao;

    @Test
    public void test() {
       List<ServerGroupDO> list= serverGroupDao.queryServerGroup();
       //     ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupByName("group_zebra-platform");
       for(ServerGroupDO serverGroupDO:list){

               nginxService.addNginxTcp(serverGroupDO.getId(), EnvType.EnvTypeEnum.test.getCode(), "dubbo");
       }
    }

    @Test
    public void test2() {
      String r=  nginxService.getNginxTcpServerConf(EnvType.EnvTypeEnum.test.getCode());
      System.err.println(r);
    }

}
