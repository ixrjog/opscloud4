package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ZabbixServiceImplTest {


    @Resource
    private UserDao userDao;

    @Autowired
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ZabbixServiceImpl zabbixServiceImpl;



    @Test
    public void testUser() {
        UserDO userDO = userDao.getUserByName("zifeng");
        System.err.println(zabbixServiceImpl.userCreate(userDO));
    }

    @Test
    public void testUser2() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_zebra");
        System.err.println(zabbixServiceImpl.actionCreate(serverGroupDO));
    }



}
