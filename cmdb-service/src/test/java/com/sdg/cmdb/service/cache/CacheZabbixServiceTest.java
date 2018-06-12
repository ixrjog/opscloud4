package com.sdg.cmdb.service.cache;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerPerfVO;
import com.sdg.cmdb.plugin.cache.CacheZabbixService;
import com.sdg.cmdb.service.ServerPerfService;
import com.sdg.cmdb.service.impl.ServerPerfServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liangjian on 2017/3/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class CacheZabbixServiceTest {

    @Resource
    private CacheZabbixService cacheZabbixService;

    @Resource
    private ServerPerfService serverPerfService;

    @Resource
    private ServerPerfServiceImpl serverPerfServiceImpl;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ServerDao serverDao;

    @Test
    public void test() {
        String isRunning = cacheZabbixService.getKeyByString(ServerPerfServiceImpl.zabbix_cacha_status);
        if (isRunning != null && isRunning.equals("true")) {
            System.err.println("yes");
        } else {
            System.err.println("no");
        }


        System.err.println(cacheZabbixService.getKeyByString(ServerPerfServiceImpl.zabbix_cacha_status));
        // System.err.println(cacheZabbixService.getKeyByString("test"));
    }


    @Test
    public void testTask() {
        String isRunning = cacheZabbixService.getKeyByString(ServerPerfServiceImpl.zabbix_cacha_status);
        if (isRunning != null && isRunning.equals("true")) {
            System.err.println("ZabbixCacheTask : task is running");
            return;
        }
        cacheZabbixService.set(ServerPerfServiceImpl.zabbix_cacha_status, "true");
        System.err.println("ZabbixCacheTask : task start");
        serverPerfService.cache();
        //cacheZabbixService.set(zabbix_cacha_status, "false");
        System.err.println("ZabbixCacheTask : end task");
    }

    @Test
    public void testInvokePerf() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_itemcenter");
        List<ServerDO> list = serverDao.acqServersByGroupId(serverGroupDO.getId());
        for (ServerDO serverDO : list) {
            if (serverDO.getEnvType() != ServerDO.EnvTypeEnum.prod.getCode()) continue;
           // if (!serverDO.getSerialNumber().equals("1")) continue;
            ServerPerfVO serverPerfVO = new ServerPerfVO(serverDO, serverGroupDO);
            serverPerfServiceImpl.invokePerf(serverPerfVO, serverDO);
            System.err.println(serverPerfVO.perfInfo());
        }


    }


}
