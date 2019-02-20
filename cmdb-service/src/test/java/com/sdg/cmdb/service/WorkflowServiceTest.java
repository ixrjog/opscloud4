package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.cmdb.WorkflowDao;

import com.sdg.cmdb.domain.workflow.WorkflowTodoDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class WorkflowServiceTest {


    @Resource
    private WorkflowDao workflowDao;

    @Resource
    private WorkflowService workflowService;



    @Test
    public void testDaoUpdateTodo() {
      WorkflowTodoDO todo = workflowDao.getTodo(126);
      if(todo == null) return ;

      todo.setContent("123456");
      System.err.println(todo);
      workflowDao.updateTodo(todo);


      todo = workflowDao.getTodo(126);
        System.err.println(todo);
    }
}
