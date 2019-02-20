package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.server.ServerGroupVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ServerGroupServiceTest {


    @Resource
    private ServerGroupService serverGroupService;

    @Test
    public void test() {
        TableVO<List<ServerGroupVO>> server = serverGroupService.queryUnauthServerGroupPage(0,100,"",-1);


        System.err.println(server);

    }

}
