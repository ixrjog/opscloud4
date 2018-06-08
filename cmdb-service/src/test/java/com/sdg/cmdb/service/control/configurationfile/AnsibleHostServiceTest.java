package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.dao.cmdb.ConfigDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2017/4/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AnsibleHostServiceTest {

    @Resource
    private AnsibleHostService ansibleHostService;

    @Resource
    protected ServerDao serverDao;

    @Resource
    protected ServerGroupDao serverGroupDao;

    @Resource
    protected ConfigDao configDao;

    @Resource
    protected UserDao userDao;

    @Test
    public void testGrouping() {
        //分组测试
//        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_trade");
//        Map<String, List<ServerDO>> serverMap = ansibleHostService.grouping(serverGroupDO);
//        for (String key : serverMap.keySet()) {
//            System.err.println("group:" + key);
//            List<ServerDO> servers =  serverMap.get(key);
//            for(ServerDO serverDO: servers)
//                System.err.println(serverDO.acqServerName());
//        }
    }


    @Test
    public void testAcqHostCfg() {

      //  System.err.println(ansibleHostService.acqHostsCfg());

        System.err.println(ansibleHostService.acqHostsAllCfg());

    }


}
