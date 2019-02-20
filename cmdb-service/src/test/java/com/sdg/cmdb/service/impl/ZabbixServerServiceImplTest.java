package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.zabbix.ZabbixTemplateDO;
import com.sdg.cmdb.domain.zabbix.ZabbixVersion;
import com.sdg.cmdb.domain.zabbix.response.ZabbixResponseHost;
import com.sdg.cmdb.domain.zabbix.response.ZabbixResponseTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ZabbixServerServiceImplTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private ServerGroupDao serverGroupDao;

    @Autowired
    private ZabbixServerServiceImpl zabbixServer;


    @Test
    public void testCheck() {
        zabbixServer.checkAuth();
    }


    @Test
    public void testGetHost() {
        ServerDO serverDO = serverDao.getServerInfoById(1129);
        ZabbixResponseHost host = zabbixServer.getHost(serverDO);
        System.err.println(host);
    }

    @Test
    public void testGetHostTemplates() {
        ServerDO serverDO = serverDao.getServerInfoById(159);
        List<ZabbixResponseTemplate> list = zabbixServer.getHostTemplates(serverDO);
        for (ZabbixResponseTemplate t : list)
            System.err.println(t);
    }

    @Test
    public void testGetZabbixVersion() {
        ZabbixVersion zv = zabbixServer.getZabbixVersion(null);
        System.err.println(zv);
    }


//    @Test
//    public void testQueryTemplate() {
//        List<ZabbixTemplateDO> templateList = zabbixServer.queryTemplate();
//        for (ZabbixTemplateDO t : templateList)
//            System.err.println(t);
//    }
//
//    @Test
//    public void testQueryProxy() {
//        List<ZabbixProxy> proxyList = zabbixServer.queryProxy();
//        for (ZabbixProxy p : proxyList)
//            System.err.println(p);
//    }
//
//    @Test
//    public void testGetUsergroup() {
//        ZabbixResponseUsergroup usergroup = zabbixServer.getUsergroup("users_nginx");
//        System.err.println(usergroup);
//    }

}
