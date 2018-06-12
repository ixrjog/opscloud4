package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.LogCleanupDao;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.logCleanup.LogCleanupPropertyDO;
import com.sdg.cmdb.service.LogCleanupService;
import com.sdg.cmdb.util.TimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by liangjian on 2017/3/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class LogCleanupServiceTest {


    @Resource
    private LogCleanupService logCleanupService;

    @Resource
    private LogCleanupServiceImpl logCleanupServiceImpl;

    @Resource
    private ServerDao serverDao;

    @Resource
    private LogCleanupDao logCleanupDao;

    @Test
    public void testAcqConfigByServerAndKey() {

        logCleanupService.syncData();
    }

    @Test
    public void test1() {
        System.err.println(new Date());
        System.err.println(new Date().toString());
    }

    @Test
    public void test() {
        try {
            long date1 = TimeUtils.dateToStamp("2017-03-31 15:51:00");
            long date2 = new Date().getTime();
            System.err.println(date1);
            System.err.println(date2);
            System.err.println(date2 - date1);

        } catch (Exception e) {

        }
    }

    @Test
    public void testTask() {
        logCleanupService.task();
    }


    @Test
    public void test2() {

        LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(402l);
        System.err.println(logCleanupServiceImpl.checkTime(logCleanupPropertyDO));

    }

    @Test
    public void test3() {
        LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(533l);
        System.err.println(logCleanupPropertyDO);
        logCleanupServiceImpl.calHistory(logCleanupPropertyDO);
        System.err.println(logCleanupPropertyDO);

    }


    @Test
    public void testCheckTime() {
        LogCleanupPropertyDO logCleanupPropertyDO = logCleanupDao.getLogCleanupPropertyByServerId(243l);
        System.err.println(logCleanupPropertyDO);


         // 248393622
         // 43200000
        System.err.println( logCleanupServiceImpl.checkTime(logCleanupPropertyDO)) ;


    }


}
