package com.sdg.cmdb.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerGroupHostPattern;
import com.sdg.cmdb.domain.server.ServerGroupVO;
import com.sdg.cmdb.service.configurationProcessor.AnsibleFileProcessorService;
import com.sdg.cmdb.util.SessionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ServerGroupServiceTest {


    @Autowired
    private ServerGroupService serverGroupService;

    @Autowired
    private ServerGroupDao serverGroupDao;
    @Autowired
    private  AnsibleFileProcessorService ansibleService;


    @Test
    public void test() {
        TableVO<List<ServerGroupVO>> server = serverGroupService.queryUnauthServerGroupPage(0,100,"",-1);
        System.err.println(server);
    }

    @Test
    public void test2() {
        int n= serverGroupDao.getMyGroupSize("baiyi");
        System.err.println(n);
    }

    @Test
    public void test3() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_opscloud");
        Map<String, List<ServerDO>> map = serverGroupService.getHostPatternFilterMap(serverGroupDO.getId());
        printJson(JSON.toJSONString(map));
    }

    @Test
    public void test4() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_opscloud");
        Map<String, List<ServerDO>> map =  ansibleService.grouping(serverGroupDO,true);
        printJson(JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect));
    }


    @Test
    public void test5() {
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_opscloud");
        Map<String, List<ServerDO>> map =  serverGroupService.getHostPatternMap(serverGroupDO.getId());
        printJson(JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect));
    }

    @Test
    public void test6() {
//        HashMap<String, List<ServerGroupHostPattern>> map =  serverGroupService.queryMyServerGroupList("pro");
//        printJson(JSON.toJSONString(map));
    }

    private void printJson(String json){
        try{
            ObjectMapper mapper1 = new ObjectMapper();
            Object obj = mapper1.readValue(json, Object.class);
            System.err.println( mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        }catch (Exception e){

        }

    }



}
