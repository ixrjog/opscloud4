package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.cmdb.ServerDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.server.ServerVO;
import com.sdg.cmdb.service.impl.ConfigServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ConfigServiceTest {


    @Resource
    private ConfigService configService;

    @Resource
    private ConfigServiceImpl configServiceImpl;

    @Resource
    private ServerDao serverDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("OPSCLOUD_TEST:A","B");
    }


    @Test
    public void testAcqConfigByServerAndKey() {
        ServerDO serverDO = new ServerDO();
        serverDO.setId(20l);
        String value = configService.acqConfigByServerAndKey(serverDO, "ZABBIX_DISK_SYS_VOLUME_NAME");
        if (value == null) {
            System.err.println("null");
        } else {
            System.err.println(value);
        }
    }

    @Test
    public void testAcqConfigByServerGroupAndKey() {
        ServerGroupDO serverGroupDO = new ServerGroupDO();
        serverGroupDO.setId(126l);
        String value = configService.acqConfigByServerGroupAndKey(serverGroupDO, "TOMCAT_APP_NAME_OPT");
        if (value == null) {
            System.err.println("null");
        } else {
            System.err.println(value);
        }
    }

    @Test
    public void testCheckServerGroupProperty() {
        configServiceImpl.checkServerGroupProperty(7);
    }


    @Test
    public void test() {
        ServerDO serverDO = serverDao.getServerInfoById(805);
        System.err.println(serverDO);

        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(serverDO.getServerGroupId());
        System.err.println(serverGroupDO);


        ServerVO serverVO = new ServerVO(serverDO, serverGroupDO);
        configServiceImpl.invokeServerConfig(serverVO);


    }



    @Test
    public void testBuildGetwayHost(){
        configService.buildGetwayHost();
    }

}
