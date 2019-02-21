package com.sdg.cmdb.service;


import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.response.*;
import com.sdg.cmdb.service.impl.ZabbixServerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ZabbixServerServiceTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private ServerGroupDao serverGroupDao;

    @Autowired
    private ZabbixServerService zabbixServer;


    @Test
    public void testGetHost() {
        ServerDO serverDO = serverDao.getServerInfoById(1129);
        ZabbixResponseHost host = zabbixServer.getHost(serverDO);
        System.err.println(host);
    }

    /**
     * 查询服务器的模版（数据来源Zabbix）
     */
    @Test
    public void testGetHostTemplates() {
        ServerDO serverDO = serverDao.getServerInfoById(1129);
        List<ZabbixResponseTemplate> tList = zabbixServer.getHostTemplates(serverDO);
        for (ZabbixResponseTemplate t : tList)
            System.err.println(t);
    }


    /**
     * 按模版名称查询模版（数据来源Zabbix）
     */
    @Test
    public void testGetTemplate() {
        // Template Nginx
        ZabbixResponseTemplate template = zabbixServer.getTemplate("Template Nginx");
        System.err.println(template);
    }

    /**
     * 查询所有的模版（数据来源Zabbix）
     */
    @Test
    public void testQueryTemplates() {
        List<ZabbixResponseTemplate> tList = zabbixServer.queryTemplates();
        for (ZabbixResponseTemplate t : tList)
            System.err.println(t);
    }

    /**
     * 查询用户组
     */
    @Test
    public void testGetUsergroup() {
        String usergroup = "users_nginx";
        ZabbixResponseUsergroup zabbixUsergroup = zabbixServer.getUsergroup(usergroup);
        System.err.println(zabbixUsergroup);
    }

    /**
     * 创建用户组
     */
    @Test
    public void testCreateUsergroup() {
        ServerGroupDO serverGroupDO = new ServerGroupDO("users_nginx");
        String usrgrpid = zabbixServer.createUsergroup(serverGroupDO);
        System.err.println(usrgrpid);
    }

    /**
     * 创建-告警动作
     */
    @Test
    public void testCreateAction() {
        ServerGroupDO serverGroupDO = new ServerGroupDO("users_nginx");
        String actionid = zabbixServer.createAction(serverGroupDO);
        System.err.println(actionid);
    }

    /**
     * 查询-告警动作
     */
    @Test
    public void testGetAction() {
        ServerGroupDO serverGroupDO = new ServerGroupDO("users_nginx");
        ZabbixResponseAction action = zabbixServer.getAction(serverGroupDO);
        System.err.println(action);
    }

    /**
     * 创建-主机组
     */
    @Test
    public void testCreateHostgroup() {
        //ServerGroupDO serverGroupDO = new ServerGroupDO("group_nginx");
        String groupid = zabbixServer.createHostgroup("group_nginx");
        System.err.println(groupid);

        ServerDO serverDO = serverDao.getServerInfoById(1129);
        groupid = zabbixServer.createHostgroup(serverDO);
        System.err.println(groupid);
    }

    /**
     * 查询-主机组
     */
    @Test
    public void testGetHostgroup() {
        ZabbixResponseHostgroup hostgroup = zabbixServer.getHostgroup("group_nginx");
        System.err.println(hostgroup);
    }


    /**
     * 禁用启用服务器监控
     */
    @Test
    public void testUpdateHostStatus() {
        //停用主机监控
        //public static final int hostStatusDisable = 1;
        //启用主机监控
        //public static final int hostStatusEnable = 0;
        ServerDO serverDO = serverDao.getServerInfoById(1129);
        // TODO 先禁用再启用
        System.err.println(zabbixServer.updateHostStatus(serverDO, 1));
        System.err.println(zabbixServer.updateHostStatus(serverDO, 0));

    }

    /**
     * 查询用户
     */
    @Test
    public void testGetUser() {
        UserDO userDO = userDao.getUserByName("baiyi");
        ZabbixResponseUser zabbixUser = zabbixServer.getUser(userDO);
        System.err.println(zabbixUser);
    }

    @Test
    public void testQueryUser() {
        List<ZabbixResponseUser> userList = zabbixServer.queryUser();
        for (ZabbixResponseUser user : userList)
            System.err.println(user);
    }


    @Test
    public void testUser() {
        UserDO userDO = new UserDO();
        userDO.setUsername("test001");
        userDO.setDisplayName("这是测试账户");
        userDO.setMobile("13456768000");
        userDO.setMail("13456768000@qq.com");
        userDO.setPwd("13456768000");
        System.err.println(zabbixServer.createUser(userDO));
        userDO.setMobile("13456768888");
        System.err.println(zabbixServer.updateUser(userDO));
        System.err.println(zabbixServer.deleteUser(userDO));
    }


    @Test
    public void testQueryProxys() {
        List<ZabbixResponseProxy> list = zabbixServer.queryProxys();
        for (ZabbixResponseProxy p : list)
            System.err.println(p);
    }

    @Test
    public void testGetProxy() {
        // ZabbixResponseProxy getProxy(String hostname)
        ZabbixResponseProxy p = zabbixServer.getProxy("proxy-test");
        System.err.println(p);
    }

    @Test
    public void testUpdateHostTemplates() {
        // item-center-1
        ServerDO serverDO = serverDao.getServerInfoById(559);
        zabbixServer.updateHostTemplates(serverDO);
    }

    @Test
    public void testUpdateHostMacros() {
        // item-center-1
        ServerDO serverDO = serverDao.getServerInfoById(559);
        zabbixServer.updateHostMacros(serverDO);
    }


    /**
     *
     ZabbixResponseItem getItem(ServerDO serverDO, String itemName, String itemKey);
     JSONObject getHistory(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit);
     JSONObject getHistory(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill);
     int checkUserInUsergroup(UserDO userDO, ServerGroupDO serverGroupDO);
     */


}
