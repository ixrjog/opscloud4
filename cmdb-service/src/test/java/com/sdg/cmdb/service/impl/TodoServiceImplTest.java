package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.TodoDao;
import com.sdg.cmdb.domain.todo.TodoDetailDO;
import com.sdg.cmdb.domain.todo.TodoDetailVO;
import com.sdg.cmdb.service.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/9/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class TodoServiceImplTest {


    @Resource
    private TodoServiceImpl todoServiceImpl;

    @Resource
    private TodoDao todoDao;

    @Resource
    private EmailService emailService;


    @Test
    public void test() {
        List<TodoDetailVO> list = todoServiceImpl.queryMyTodoJob("by");
//        for(TodoDetailVO vo:list){
//            System.err.println(vo);
//        }
    }

    @Test
    public void test2() {
        TodoDetailDO todoDetailDO = todoDao.queryTodoDetailById(24l);
        TodoDetailVO todoDetailVO = new TodoDetailVO(todoDetailDO);
        todoServiceImpl.invokeTodoDetailVO(todoDetailVO);
        System.err.println(todoDetailVO);
        emailService.doSendSubmitTodo(todoDetailVO);


    }

    @Test
    public void test3() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("A", "B");
        map.put("C", 1000000);

        System.err.println(map.get("C").toString());


    }


}
