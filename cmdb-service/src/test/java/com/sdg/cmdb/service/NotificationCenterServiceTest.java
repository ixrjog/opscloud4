package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class NotificationCenterServiceTest {

    @Resource
    private NotificationCenterService ncService;

    @Resource
    private WorkflowService workflowService;

    @Resource
    private UserDao userDao;

    @Test
    public void test() {
        WorkflowTodoVO todoVO = workflowService.getTodo(303);
        ncService.notifWorkflowTodo(todoVO,userDao.getUserByName("baiyi"));
    }

}
