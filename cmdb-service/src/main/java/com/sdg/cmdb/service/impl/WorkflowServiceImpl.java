package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.dao.cmdb.WorkflowDao;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.*;
import com.sdg.cmdb.factory.workflow.TodoAbs;
import com.sdg.cmdb.factory.workflow.WorkflowTodoFactory;
import com.sdg.cmdb.service.WorkflowService;
import com.sdg.cmdb.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Autowired
    private WorkflowDao workflowDao;

    @Autowired
    private UserDao userDao;

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
    public BusinessWrapper<Boolean> revokeTodo(long todoId) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);

        WorkflowTodoDO todoDO = workflowDao.getTodo(todoId);
        if (todoDO != null && todoDO.getApplyUserId() == userDO.getId()) {
            try {
                workflowDao.delTodo(todoId);
                return new BusinessWrapper<Boolean>(true);
            } catch (Exception e) {
                return new BusinessWrapper<Boolean>(false);
            }
        } else {
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public BusinessWrapper<Boolean> invokeTodo(long todoId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowDO workflowDO = workflowDao.getWorkflow(workflowTodoDO.getWfId());
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(workflowDO.getWfKey());
        return new BusinessWrapper<Boolean>(todoAbs.invokeTodo(todoId));
    }

    /**
     * 审批工单
     *
     * @param todoId
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> approvalTodo(long todoId) {
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(getWorkflowKey(todoId));
        return new BusinessWrapper<Boolean>(todoAbs.approvalTodo(todoId));
    }

    private String getWorkflowKey(long todoId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowDO workflowDO = workflowDao.getWorkflow(workflowTodoDO.getWfId());
        return workflowDO.getWfKey();
    }


    @Override
    public WorkflowTodoVO delTodoDetail(long todoId, long detailId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowDO workflowDO = workflowDao.getWorkflow(workflowTodoDO.getWfId());
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(workflowDO.getWfKey());
        return todoAbs.delTodoDetail(todoId, detailId);
    }

    /**
     * 申请
     *
     * @param todoId
     * @return
     */
    @Override
    public WorkflowTodoVO applyTodo(long todoId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowDO workflowDO = workflowDao.getWorkflow(workflowTodoDO.getWfId());
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(workflowDO.getWfKey());
        return todoAbs.applyTodo(workflowDO, workflowTodoDO);
    }

    @Override
    public List<WorkflowTodoVO> queryMyTodo() {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        HashMap<Long, WorkflowTodoDO> todoMap = new HashMap<>();
        List<WorkflowTodoDO> todos = workflowDao.queryTodoByApplyUserId(userDO.getId());
        todoMap = todo2map(todos, todoMap);
        return toVO(todos);
    }

    private List<WorkflowTodoVO> toVO(List<WorkflowTodoDO> todos) {
        List<WorkflowTodoVO> voList = new ArrayList<>();
        for (WorkflowTodoDO workflowTodoDO : todos) {
            WorkflowDO workflowDO = workflowDao.getWorkflow(workflowTodoDO.getWfId());
            TodoAbs todoAbs = WorkflowTodoFactory.getByKey(workflowDO.getWfKey());
            voList.add(todoAbs.getTodo(workflowTodoDO.getId()));
        }
        return voList;
    }


    /**
     * todo去重（避免申请人与审批人相同的重复操作）
     *
     * @param todos
     * @param todoMap
     * @return
     */
    private HashMap<Long, WorkflowTodoDO> todo2map(List<WorkflowTodoDO> todos, HashMap<Long, WorkflowTodoDO> todoMap) {
        for (WorkflowTodoDO todo : todos)
            todoMap.put(todo.getId(), todo);
        return todoMap;
    }


}
