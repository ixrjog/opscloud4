package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.WorkflowDao;

import com.sdg.cmdb.domain.workflow.*;
import com.sdg.cmdb.factory.workflow.TodoAbs;
import com.sdg.cmdb.factory.workflow.WorkflowTodoFactory;
import com.sdg.cmdb.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Resource
    private WorkflowDao workflowDao;

    //private WorkflowTodoFactory workflowTodoFactory;

    @Override
    public List<WorkflowGroupVO> queryWorkflowGroup(String topics) {
        List<WorkflowGroupDO> groups = workflowDao.getWorkflowGroup();

        List<WorkflowGroupVO> voList = new ArrayList<>();
        for (WorkflowGroupDO group : groups) {
            List<WorkflowDO> wfs = workflowDao.queryWorkflowByGroupIdAndTopics(group.getId(), topics);
            WorkflowGroupVO groupVO = new WorkflowGroupVO(group, wfs);
            voList.add(groupVO);
        }
        return voList;
    }

    /**
     * 创建工单
     *
     * @param wfKey
     * @return
     */
    @Override
    public WorkflowTodoVO createTodo(String wfKey) {
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(wfKey);
        return todoAbs.createTodo();
    }

    @Override
    public WorkflowTodoVO saveTodo(WorkflowTodoVO workflowTodoVO) {
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(workflowTodoVO.getWorkflowDO().getWfKey());
        return todoAbs.saveTodo(workflowTodoVO);
    }

    @Override
    public WorkflowTodoVO delTodoDetail(long todoId, long detailId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowDO workflowDO = workflowDao.getWorkflow(workflowTodoDO.getWfId());
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(workflowDO.getWfKey());
        return todoAbs.delTodoDetail(todoId, detailId);
    }

    @Override
    public WorkflowTodoVO applyTodo(long todoId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowDO workflowDO = workflowDao.getWorkflow(workflowTodoDO.getWfId());
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(workflowDO.getWfKey());
        return todoAbs.applyTodo(workflowDO,workflowTodoDO);
    }


}
