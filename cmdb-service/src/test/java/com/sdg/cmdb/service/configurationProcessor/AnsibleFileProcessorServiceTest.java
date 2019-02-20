package com.sdg.cmdb.service.configurationProcessor;

import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AnsibleFileProcessorServiceTest {

    @Autowired
    private AnsibleFileProcessorService ansibleService;

    @Autowired
    private ServerGroupDao serverGroupDao;


    @Test
    public void testAll() {
        System.err.println(ansibleService.getConfig(0, true));
    }


    @Test
    public void testServerGroup() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_trade-zebra");
        Map<String, List<ServerDO>> map = ansibleService.grouping(serverGroupDO, true);
        for (String key : map.keySet()) {
            System.err.println("hostPattern:" + key);
            List<ServerDO> servers = map.get(key);
            for (ServerDO server : servers)
                System.err.println(server.acqServerName());
        }
    }
}
