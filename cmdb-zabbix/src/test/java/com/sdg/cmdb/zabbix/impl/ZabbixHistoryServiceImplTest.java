package com.sdg.cmdb.zabbix.impl;

import com.alibaba.fastjson.JSONObject;
import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.zabbix.ZabbixHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by liangjian on 2016/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ZabbixHistoryServiceImplTest {

    @Resource
    protected ZabbixHistoryServiceImpl zabbixHistoryServiceImpl;

    @Resource
    protected ZabbixHistoryService zabbixHistoryService;

    @Resource
    protected ServerDao serverDao;

    @Resource
    protected ServerGroupDao serverGroupDao;



    @Test
    public void testQueryServerGroupByName() {
        ServerGroupDO serverGroupDO=serverGroupDao.queryServerGroupByName("group_zsearch-solr-engine");
        System.err.println(zabbixHistoryServiceImpl.queryHistory(serverGroupDO));
    }

    @Test
    public void testQueryCpuUser() {
        ServerDO serverDO = serverDao.getServerInfoById(31);
        JSONObject response=zabbixHistoryService.queryCpuUser(serverDO, 1);
        String value=zabbixHistoryService.acqResultValue(response);
        System.err.println(value);
    }

    @Test
    public void testQueryMem() {
        ServerDO serverDO = serverDao.getServerInfoById(144);
        String memoryAvailable = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryMemoryAvailable(serverDO, 1));
        String memoryTotal = zabbixHistoryService.acqResultValue(zabbixHistoryService.queryMemoryTotal(serverDO, 1));
        long memoryRate = Long.parseLong(memoryAvailable) * 100 / Long.parseLong(memoryTotal);
        System.err.println(memoryRate);

    }

    @Test
    public void testQueryDisk() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_itemcenter");
        List<ServerDO> list = serverDao.acqServersByGroupId(serverGroupDO.getId());
        for (ServerDO serverDO : list) {
            if (serverDO.getEnvType() != ServerDO.EnvTypeEnum.prod.getCode()) continue;
            if(!serverDO.getSerialNumber().equals("1")) continue;
            String freeDiskSpaceOnData = zabbixHistoryService.acqResultValue(zabbixHistoryServiceImpl.queryFreeDiskSpaceOnData(serverDO, 1));
            System.err.println(serverDO.getServerName()+serverDO.getSerialNumber()+"FreeDiskSpaceOnData:" +freeDiskSpaceOnData );
            System.err.println(zabbixHistoryServiceImpl.queryFreeDiskSpaceOnData(serverDO, 1));
            System.err.println(zabbixHistoryServiceImpl.queryFreeDiskSpaceOnSys(serverDO, 1));
        }

    }

    @Test
    public void testQueryTomcatVersion() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_member");
        List<ServerDO> list = serverDao.acqServersByGroupId(serverGroupDO.getId());
        for (ServerDO serverDO : list) {
            //if (serverDO.getEnvType() != ServerDO.EnvTypeEnum.prod.getCode()) continue;
            //if(!serverDO.getSerialNumber().equals("1")) continue;
            JSONObject jo= zabbixHistoryServiceImpl.queryTomcatVersion(serverDO, 1);
            String version = zabbixHistoryService.acqResultValue(jo);
            System.err.println(jo);
            System.err.println(serverDO.acqServerName()+"TomcatVersion : " +version);

        }

    }

//    @Test
//    public void testItems() {
//
//        //TODO 查询主机
//        ServerDO serverDO = serverDao.getServerInfoById(31);
//        System.err.println(serverDO.getId());
//        zabbixServiceHistoryImpl.set(serverDO);
//        System.err.println("CpuUser CPU使用率(单位%) : " + zabbixServiceHistoryImpl.queryHistoryCpuUser(10));
//        System.err.println("CpuUser CPU使用率(单位%) : " + zabbixServiceHistoryImpl.queryHistoryCpuUser(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("PerCpuAvg 处理器load(1分钟平均值) : " + zabbixServiceHistoryImpl.queryHistoryPerCpuAvg(10));
//        System.err.println("PerCpuAvg 处理器load(1分钟平均值) : " + zabbixServiceHistoryImpl.queryHistoryPerCpuAvg(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("IncomingNetworkTrafficOnEth0 eth0流入 : " + zabbixServiceHistoryImpl.queryHistoryIncomingNetworkTrafficOnEth0(10));
//        System.err.println("IncomingNetworkTrafficOnEth0 eth0流入 : " + zabbixServiceHistoryImpl.queryHistoryIncomingNetworkTrafficOnEth0(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("OutgoingNetworkTrafficOnEth0 eth0流出 : " + zabbixServiceHistoryImpl.queryHistoryOutgoingNetworkTrafficOnEth0(10));
//        System.err.println("OutgoingNetworkTrafficOnEth0 eth0流出 : " + zabbixServiceHistoryImpl.queryHistoryOutgoingNetworkTrafficOnEth0(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("IncomingNetworkTrafficOnEth1 eth1流入 : " + zabbixServiceHistoryImpl.queryHistoryIncomingNetworkTrafficOnEth1(10));
//        System.err.println("IncomingNetworkTrafficOnEth1 eth1流入 : " + zabbixServiceHistoryImpl.queryHistoryIncomingNetworkTrafficOnEth1(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("OutgoingNetworkTrafficOnEth1 eth1流出 : " + zabbixServiceHistoryImpl.queryHistoryOutgoingNetworkTrafficOnEth1(10));
//        System.err.println("OutgoingNetworkTrafficOnEth1 eth1流出 : " + zabbixServiceHistoryImpl.queryHistoryOutgoingNetworkTrafficOnEth1(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("TotalDiskSpaceOnSys 系统盘总容量 : " + zabbixServiceHistoryImpl.queryHistoryTotalDiskSpaceOnSys(10));
//        System.err.println("TotalDiskSpaceOnSys 系统盘总容量 : " + zabbixServiceHistoryImpl.queryHistoryTotalDiskSpaceOnSys(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("UsedDiskSpaceOnSys 系统盘使用量 : " + zabbixServiceHistoryImpl.queryHistoryUsedDiskSpaceOnSys(10));
//        System.err.println("UsedDiskSpaceOnSys 系统盘使用量 : " + zabbixServiceHistoryImpl.queryHistoryUsedDiskSpaceOnSys(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("TotalDiskSpaceOnData 数据盘总容量 : " + zabbixServiceHistoryImpl.queryHistoryTotalDiskSpaceOnData(10));
//        System.err.println("TotalDiskSpaceOnData 数据盘总容量 : " + zabbixServiceHistoryImpl.queryHistoryTotalDiskSpaceOnData(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//        System.err.println("UsedDiskSpaceOnData 数据盘使用量 : " + zabbixServiceHistoryImpl.queryHistoryUsedDiskSpaceOnData(10));
//        System.err.println("UsedDiskSpaceOnData 数据盘使用量 : " + zabbixServiceHistoryImpl.queryHistoryUsedDiskSpaceOnData(date2TimeStamp("2016-12-21 16:00:00"), date2TimeStamp("2016-12-21 18:00:00")));
//
//    }


    public static String date2TimeStamp(String time) {
        String format = "yyyy-MM-dd HH:mm:ss";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(time).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
