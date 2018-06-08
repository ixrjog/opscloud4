package com.sdg.cmdb.zabbix.impl;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.ZabbixProxy;
import com.sdg.cmdb.domain.zabbix.ZabbixTemplateDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangjian on 2016/12/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ZabbixServiceImplTest {

    @Resource
    protected ZabbixServiceImpl zabbixServiceImpl;

    @Resource
    protected ServerDao serverDao;

    @Resource
    protected UserDao userDao;

    @Resource
    protected ServerGroupDao serverGroupDao;


    @Test
    public void testVersion() {
        System.err.println(zabbixServiceImpl.getZabbixVersion(""));

    }

    @Test
    public void testTemplateQueryAll() {
        List<ZabbixTemplateDO> temps =zabbixServiceImpl.templateQueryAll();
        for(ZabbixTemplateDO t:temps){
            System.err.println(t);
        }
    }

    // proxyQueryAll
    @Test
    public void testProxyQueryAll() {
    List<ZabbixProxy> list =zabbixServiceImpl.proxyQueryAll();
        for(ZabbixProxy p:list){
            System.err.println(p);
        }

    }


    @Test
    public void testRsyncTemplate() {
     zabbixServiceImpl.rsyncTemplate();
    }

    @Test
    public void testUsergroupCreate() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_baoherp");
        System.err.println(zabbixServiceImpl.usergroupCreate(serverGroupDO));
    }

    @Test
    public void testUserCreate() {
        // 	15158116767  chenyuyu@51xianqu.net
        UserDO userDO = userDao.getUserByName("zhonghongsheng");
        System.err.println(userDO);
        System.err.println(zabbixServiceImpl.userCreate(userDO));

    }

    @Test
    public void testUserDelete() {
        // 	15158116767  chenyuyu@51xianqu.net
        UserDO userDO = userDao.getUserByName("chenyuyu");
        System.err.println(userDO);
        System.err.println(zabbixServiceImpl.userDelete(userDO));
    }

    @Test
    public void testSyncUser() {
        zabbixServiceImpl.syncUser();
        // zabbixServiceImpl.checkZabbixUser();
    }

    @Test
    public void testUserUpdate() {
        UserDO userDO = userDao.getUserByName("chenyuyu");
        zabbixServiceImpl.userUpdate(userDO);
    }

    @Test
    public void testCi() {
        /**
         *         ci(String key, int type,
         *         String project, String group,
         *         String env, Long deployId,
         *         String bambooDeployVersion,
         int bambooBuildNumber,
         String bambooDeployProject, boolean bambooDeployRollback,
         String bambooManualBuildTriggerReasonUserName, int errorCode, String branchName, int deployType)
         */

        //System.err.println(zabbixServiceImpl.ci("7ab6ed9458b3cb477ecee026d85e5b42",ZabbixServiceImpl.hostStatusDisable,"cmdb","cmdb-production","production",11111112L));
        zabbixServiceImpl.ci("7ab6ed9458b3cb477ecee026d85e5b42", ZabbixServiceImpl.hostStatusEnable,
                "trade", "trade-production-1",
                "production", 11111112L,
                "release-1",
                1, "trade-master", false,
                "liangjian", 0, "master", 0);
    }

    @Test
    public void testActionGet() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_shop");
        System.err.println(zabbixServiceImpl.actionGet(serverGroupDO));
    }


    @Test
    public void testActionCreate() {
        //ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_msggetway");
        ServerGroupDO serverGroupDO = new ServerGroupDO();
        serverGroupDO.setName("group_sdop");
        System.err.println(zabbixServiceImpl.actionCreate(serverGroupDO));

    }

//    @Test
//    public void testHost() {
//        //查询主机
//        ServerDO serverDO = serverDao.getServerInfoById(21);
//        System.err.println(serverDO.getId());
//        zabbixServiceImpl.set(serverDO);
//        System.err.println("hostExists : " + zabbixServiceImpl.hostExists());
//        System.err.println("hostGet : " + zabbixServiceImpl.hostGet());
//        System.err.println("hostGetByIP : " + zabbixServiceImpl.hostGetByIP("10.117.206.167"));
//        System.err.println("hostGetByName : " + zabbixServiceImpl.hostGetByName("proxy1.zabbix3.51xianqu.net"));
//
//        // System.err.println("hostUpdateStatus : " + zabbixServiceImpl.hostUpdateStatus(ZabbixServiceImpl.hostStatusDisable));
//        System.err.println("hostUpdateStatus : " + zabbixServiceImpl.hostUpdateStatus(ZabbixServiceImpl.hostStatusEnable));
//    }

    @Test
    public void testHostDelete() {
        System.err.println("hostCreate : " + zabbixServiceImpl.delMonitor(336));
    }

    @Test
    public void testHostCreate() {
        System.err.println("hostCreate : " + zabbixServiceImpl.addMonitor(510));
    }

    @Test
    public void testHostExists() {
        ServerDO serverDO = serverDao.getServerInfoById(336);
        System.err.println("hostExists : " + zabbixServiceImpl.hostExists(serverDO));
    }

    @Test
    public void testHostGetStatus() {
        ServerDO serverDO = serverDao.getServerInfoById(336);
        System.err.println("hostGetStatus : " + zabbixServiceImpl.hostGetStatus(serverDO));
    }


    //    @Test
//    public void testProxy() {
//        //TODO 这里写用例代码
//        ServerDO serverDo = serverDao.getServerInfoById(20);
//        zabbixServiceImpl.set(serverDo);
//        System.err.println("proxyGet : " + zabbixServiceImpl.proxyGet());
//        System.err.println("proxyGet : " + zabbixServiceImpl.proxyGet("proxy1.zabbix3.51xianqu.net"));
//    }
//
//    @Test
//    public void testHostgroup() {
//        //TODO 这里写用例代码
//        ServerDO serverDo = serverDao.getServerInfoById(20);
//        zabbixServiceImpl.set(serverDo);
//
//        System.err.println("hostgroupExists : " + zabbixServiceImpl.hostgroupExists());
//        System.err.println("hostgroupCreate : " + zabbixServiceImpl.hostgroupCreate());
//        System.err.println("hostgroupGet : " + zabbixServiceImpl.hostgroupGet("group_javatest"));
//
//    }
//
//    @Test
//    public void testTemplate() {
//        //TODO 这里写用例代码
//        System.err.println("templateGet : " + zabbixServiceImpl.templateGet("Template Java"));
//        System.err.println("templateGet : " + zabbixServiceImpl.templateGet("Template OS Linux"));
//    }
//
    @Test
    public void testItems() {
        //TODO 这里写用例代码
        ServerDO serverDO = serverDao.getServerInfoById(31);
        System.err.println("itemGet : " + zabbixServiceImpl.itemGet(serverDO, "CPU", "system.cpu.util[,user]"));
    }

    @Test
    public void testApi() {
        zabbixServiceImpl.api(0, "7ab6ed9458b3cb477ecee026d85e5b42", "trade", "trade-gray", "gray");
    }

    @Test
    public void testUser() {
        //// TODO 用户是否在用户组中
        UserDO userDO = new UserDO();
        userDO.setUsername("liangjian");
        ServerGroupDO serverGroupDO = new ServerGroupDO();
        serverGroupDO.setName("group_ci");

        System.err.println(zabbixServiceImpl.checkUserInUsergroup(userDO, serverGroupDO));
    }


    @Test
    public void testCheck() {
        ServerDO serverDO = serverDao.getServerInfoById(732);
        System.err.println(zabbixServiceImpl.hostGetStatus(serverDO));

        System.err.println(zabbixServiceImpl.hostExists(serverDO));


    }


}



