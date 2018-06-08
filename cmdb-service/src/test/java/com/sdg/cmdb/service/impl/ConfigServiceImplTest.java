package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.service.KeyBoxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2016/11/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ConfigServiceImplTest {

    @Resource
    private KeyBoxService keyBoxService;

    @Test
    public void testBuildGetwayUser() {
        UserDO userDO = new UserDO();
        userDO.setUsername("by");
        userDO.setDisplayName("白衣");
        userDO.setMail("by@qq.com");

        keyBoxService.createUserGroupConfigFile(userDO.getUsername());
    }

}
