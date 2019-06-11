package com.sdg.cmdb.service;


import com.sdg.cmdb.dao.cmdb.KeyboxDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.keybox.KeyboxUserServerDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.zabbix.ZabbixProblemVO;
import com.sdg.cmdb.domain.zabbix.response.*;
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
    private KeyBoxService keyBoxService;

    @Autowired
    private KeyboxDao keyboxDao;

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

    @Test
    public void testQueryItems() {
        // item-center-1
        ServerDO serverDO = serverDao.getServerInfoById(559);
        List<ZabbixResponseItem> items = zabbixServer.queryItems(serverDO);
        for (ZabbixResponseItem item : items)
            System.err.println(item);

    }

    @Test
    public void testGetItem() {
        // item-center-1
        ServerDO serverDO = serverDao.getServerInfoById(559);
        ZabbixResponseItem itemFree = zabbixServer.getItem(serverDO, "", "vfs.fs.size[/,free]");
        System.err.println(itemFree);
        //vfs.fs.size[/,pfree]
        ZabbixResponseItem itemPfree = zabbixServer.getItem(serverDO, "", "vfs.fs.size[/,pfree]");
        System.err.println(itemPfree);

        ZabbixResponseItem item = zabbixServer.getItem("40909");
        System.err.println(item);
    }

    @Test
    public void testGetTriggers() {
        List<ZabbixResponseTrigger> list = zabbixServer.getTriggers(2);
        for (ZabbixResponseTrigger trigger : list)
            System.err.println(trigger);
    }

    @Test
    public void testGetHostinterface() {
        List<ZabbixResponseHostinterface> list = zabbixServer.getHostinterface("10874");
        for (ZabbixResponseHostinterface hostinterface : list)
            System.err.println(hostinterface);
    }


    @Test
    public void testGetProblems() {
        List<ZabbixProblemVO> list = zabbixServer.getProblems();
    }

    @Test
    public void testGetMyProblems() {
        //  List<ZabbixProblemVO>  list=   zabbixServer.getProblems("baiyi");

    }

    @Test
    public void test3() {
        zabbixServer.getDashboards();

    }

    @Test
    public void test4() {
        String x = "Abdiihgd #{HOST.NAME} BBB";
        System.err.println(x.replaceAll("#\\{HOST\\.?NAME}", "SERVER1"));

    }

    @Test
    public void test2() {
        long size = 96419217408l;
        System.err.println(size / 1024 / 1024 / 1024);
        long size2 = 795674299l;
        System.err.println(size2 / 1024 / 1024 / 1024);
    }


    @Test
    public void test10() {
        List<UserDO> userList = userDao.getAllUser();
        for (UserDO userDO : userList) {
            KeyboxUserServerDO userServerDO = new KeyboxUserServerDO(userDO.getUsername(), 0);
            long size = keyboxDao.getUserServerSize(userServerDO);
            if (size > 0) {
                System.err.println(userDO.getUsername() + " 服务器组数量:" + size);
                System.err.println(zabbixServer.createUser(userDO));
            }
        }


    }

    /**
     *
     ZabbixResponseItem getItem(ServerDO serverDO, String itemName, String itemKey);
     JSONObject getHistory(ServerDO serverDO, String itemName, String itemKey, int historyType, int limit);
     JSONObject getHistory(ServerDO serverDO, String itemName, String itemKey, int historyType, String timestampFrom, String timestampTill);
     int checkUserInUsergroup(UserDO userDO, ServerGroupDO serverGroupDO);
     */


}
