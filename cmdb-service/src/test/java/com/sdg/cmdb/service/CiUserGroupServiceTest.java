package com.sdg.cmdb.service;

import com.sdg.cmdb.plugin.chain.TaskCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by liangjian on 2017/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class CiUserGroupServiceTest {

    @Resource
    private CiUserGroupService ciUserGroupService;

    @Test
    public void test() {

        ciUserGroupService.usersRefresh();


    }

}
