package com.sdg.cmdb.service;


import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.aliyuncs.ram.model.v20150501.ListPoliciesResponse;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailRAMGroup;
import com.sdg.cmdb.factory.workflow.TodoAliyunRAM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AliyunRAMServiceTest {

    @Autowired
    private AliyunRAMService aliyunRAMService;

    @Test
    public void testGetUserGroupInfo() {
        UserDO userDO = new UserDO("baiyi");
        List<TodoDetailRAMGroup> groups = aliyunRAMService.getUserGroupInfo(userDO);
        for (TodoDetailRAMGroup group : groups) {
            System.err.println(group.getGroupName());
        }
    }


    @Test
    public void testListPolicies() {
        List<ListPoliciesResponse.Policy> list = aliyunRAMService.listPolicies(null, 1000);
        for (ListPoliciesResponse.Policy policy : list) {
            System.err.println(policy.getPolicyName());
            System.err.println(policy.getDescription());
        }
    }

    @Test
    public void testUpdateRamPolicies() {

        aliyunRAMService.updateRamPolicies();


    }

    @Test
    public void testListPoliciesForUse() {
        // gegejia_baiyi@1255805305757185.onaliyun.com
        List<ListPoliciesForUserResponse.Policy> list = aliyunRAMService.listPoliciesForUser("baiyi");
        for (ListPoliciesForUserResponse.Policy policy : list) {
            System.err.println(policy.getPolicyName());
        }
    }

    @Test
    public void testListUsers() {
        ListUsersResponse us = aliyunRAMService.listUsers(10, null);
        System.err.println(us);
    }


    @Test
    public void testList() {
        List<ListUsersResponse.User> u = aliyunRAMService.listAllUsers();
        System.err.println(u);
    }

}
