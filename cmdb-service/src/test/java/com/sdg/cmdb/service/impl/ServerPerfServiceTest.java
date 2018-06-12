package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.ServerPerfService;
import com.sdg.cmdb.service.ZabbixHistoryService;
import com.sdg.cmdb.util.TimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangjian on 2017/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ServerPerfServiceTest {


    @Resource
    private ServerPerfService serverPerfService;

    @Resource
    private ZabbixHistoryService zabbixHistoryService;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Test
    public void test() {
        ServerDO s1 = serverDao.getServerInfoById(752);
        ServerDO outway2 = serverDao.getServerInfoById(108);

        ServerDO serverDO = s1;


        System.err.println(s1);
        long sysDiskRate = 0;
        String freeDiskOnSys = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryFreeDiskSpaceOnSys(serverDO, 1));
        System.err.println("freeDiskOnSys:" + freeDiskOnSys);

        String freeDiskOnData = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryFreeDiskSpaceOnData(serverDO, 1));
        System.err.println("freeDiskOnData:" + freeDiskOnData);

        //sysDiskRate = Long.parseLong(usedDiskSpaceOnSys) * 100 / Long.parseLong(totalDiskSpaceOnSys);

        //String usedDiskSpaceOnData = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryUsedDiskSpaceOnData(serverDO, 1));
        //String totalDiskSpaceOnData = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryTotalDiskSpaceOnData(serverDO, 1));
        //long dataDiskRate = Long.parseLong(usedDiskSpaceOnData) * 100 / Long.parseLong(totalDiskSpaceOnData);

        //System.err.println("sysDiskRate:" + sysDiskRate);
        //System.err.println("dataDiskRate:" + dataDiskRate);

    }

    @Test
    public void testGetCache() {
        ServerDO itemcenter1 = serverDao.getServerInfoById(98);
        System.err.println(serverPerfService.getCache(itemcenter1).perfInfo());


    }


    @Test
    public void testCache() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_itemcenter");
        List<ServerDO> servers = serverDao.acqServersByGroupId(serverGroupDO.getId());
        ServerDO itemcenter1 = servers.get(0);
        System.err.println(itemcenter1);
        System.err.println(serverPerfService.cache(itemcenter1).perfInfo());
        //System.err.println(serverPerfService.cache(itemcenter1).toString());

    }

    @Test
    public void test2() {
        TimeUtils.nowDate();

    }

}


