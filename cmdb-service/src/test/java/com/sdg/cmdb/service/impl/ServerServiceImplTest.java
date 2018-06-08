package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.domain.server.EcsServerDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.ServerGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ServerServiceImplTest {


    @Resource
    private ServerServiceImpl serverServiceImpl;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupService serverGroupService;


    @Test
    public void testServerStatus() {

        String[] groups = {"group_kamember", "group_member",
                "group_logistics", "group_shop",
                "group_itemcore",
                "group_ib", "group_market",
                "group_delivery",
                "group_shopcart",
                "group_trade",
                "group_pay",
                "group_payservice-alipay",
                "group_payservice-wxpay",
                "group_tradeback",
                "group_ka-groupon"};

        String r = "";

        int sum = 0;

        HashMap<String, Integer> map = new HashMap<>();

        for (String groupName : groups) {
            ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupByName(groupName);
            System.err.println(serverGroupDO.getName());
            r += "服务器组名称：" + serverGroupDO.getName() + "\n";
            List<ServerDO> servers = serverServiceImpl.getServersByGroupId(serverGroupDO.getId());
            for (ServerDO serverDO : servers) {
                if (serverDO.getServerType() == ServerDO.ServerTypeEnum.ecs.getCode()) {
                    EcsServerDO ecs = serverDao.queryEcsByInsideIp(serverDO.getInsideIp());

                    String key = ecs.getCpu() + "C/" + ecs.getMemory() + "M";
                    if (map.containsKey(key)) {
                        int i = map.get(key);
                        map.put(key, i + 1);
                    } else {
                        map.put(key, 1);
                    }

                    r += serverDO.acqHostname() + ":" + key + "\n";
                    sum++;
                }
            }


        }

        System.err.println(r);
        System.err.println(sum);

        for(String key:map.keySet()){
           int i= map.get(key);
           System.err.println(key + ":" + i);
        }
    }

}
