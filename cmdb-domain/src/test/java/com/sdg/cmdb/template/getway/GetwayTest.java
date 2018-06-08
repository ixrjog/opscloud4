package com.sdg.cmdb.template.getway;

import com.sdg.cmdb.dao.cmdb.AuthDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.ServerGroupService;

import com.sdg.cmdb.util.PasswdUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by liangjian on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class GetwayTest {

//    @Resource
//    private AuthDao authDao;
//
//    @Resource
//    private ServerGroupService serverGroupService;
//
//    @Test
//    public void testGetway() {
//        /**
//         * TODO Getway(UserDO userDO, List<ServerGroupDO> serverGroups, Map<String, List<ServerDO>> servers)
//         */
//        List<UserDO> userDOList = authDao.getLoginUsers();
//
//
//
//        if (userDOList.isEmpty()) {
//            return;
//        }
//
//        UserDO userDO = userDOList.get(0);
//
//        ServerGroupDO serverGroupDO = serverGroupService.queryServerGroupByName("group_trade");
//        ServerGroupDO serverGroupDO2 = serverGroupService.queryServerGroupByName("group_member");
//        ServerGroupDO serverGroupDO3 = serverGroupService.queryServerGroupByName("group_wms");
//        List<ServerGroupDO> serverGroups = new ArrayList<>();
//        serverGroups.add(serverGroupDO);
//        serverGroups.add(serverGroupDO2);
//        serverGroups.add(serverGroupDO3);
//        Map<String, List<ServerDO>> servers = new HashMap<>();
//
////        for (ServerGroupDO groupDO : serverGroups) {
////            List<ServerDO> serverDOList = serverDao.getServerPage(Collections.EMPTY_LIST, -1, "", 0, -1, 0, 10);
////            servers.put("group_trade", serverDOList);
////            List<ServerDO> serverDOList2 = serverDao.getServerPage(Collections.EMPTY_LIST, -1, "", 10, -1, 0, 10);
////            servers.put("group_member", serverDOList2);
////            List<ServerDO> serverDOList3 = serverDao.acqServersByGroupId(8);
////            servers.put("group_wms", serverDOList3);
////        }
//
//        Getway gw = new Getway(userDO, serverGroups);
//        System.err.println("生成用户getway.conf---------------------------------------");
//        System.err.println(gw.toString());
//
//
//
//        System.err.println("生成全局列表getway_host.conf---------------------------------------");
//        Getway gw2 = new Getway(serverGroups ,servers);
//        System.err.println(gw2.toString());
//        System.err.println("---------------------------------------");
//
//    }
//
//    @Test
//    public void testGetRandomPasswd() {
//        //生成用户随机密码
//        String passwd = PasswdUtils.getRandomPasswd(0);
//        System.err.println(passwd);
//
//    }


}
