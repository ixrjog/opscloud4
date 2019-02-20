package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.TeamDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.dao.cmdb.WorkflowDao;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.*;
import com.sdg.cmdb.factory.workflow.TodoAbs;
import com.sdg.cmdb.factory.workflow.WorkflowTodoFactory;
import com.sdg.cmdb.service.AuthService;
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

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private AuthService authService;


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

    /**
     * 不批准
     * @param todoId
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> disapproveTodo(long todoId){
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(getWorkflowKey(todoId));
        return new BusinessWrapper<Boolean>(todoAbs.disapproveTodo(todoId));
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


    @Override
    public WorkflowTodoVO getTodo(long todoId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowDO workflowDO = workflowDao.getWorkflow(workflowTodoDO.getWfId());
        TodoAbs todoAbs = WorkflowTodoFactory.getByKey(workflowDO.getWfKey());
        return todoAbs.getTodo(todoId);
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
        // TODO 去重
        todoInMap(todos, todoMap);
        // TODO 如果用户是TL则插入team成员的待审批工单
        gueryTlTodo(userDO, todoMap);
        // TODO 按角色插入待审批工单
        gueryRoleTodo(userDO, todoMap);
        List<WorkflowTodoDO> myTodos = new ArrayList<>();
        for (long todoId : todoMap.keySet()) {
            myTodos.add(todoMap.get(todoId));
        }
        return toVO(myTodos);
    }

    @Override
    public List<WorkflowTodoVO> queryMyCompleteTodo() {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        List<WorkflowTodoDO> todos = workflowDao.queryTodoByApplyUserIdAndTodoPhase(userDO.getId(), WorkflowTodoDO.TODO_PHASE_COMPLETE);
        return toVO(todos);
    }

    /**
     * 查询TL待审批工作流
     *
     * @param userDO
     * @param todoMap
     */
    private void gueryTlTodo(UserDO userDO, HashMap<Long, WorkflowTodoDO> todoMap) {
        queryTeamuserTodo(userDO.getId(), todoMap);
    }

    private void queryTeamuserTodo(long teamleaderUserId, HashMap<Long, WorkflowTodoDO> todoMap) {
        TeamDO teamDO = teamDao.queryTeamByLeaderUserId(teamleaderUserId);
        if (teamDO == null) return;
        List<TeamuserDO> teamusers = teamDao.queryTeamuserByTeamId(teamDO.getId());
        for (TeamuserDO teamuser : teamusers) {
            List<WorkflowTodoDO> todos = workflowDao.queryTodoByApplyUserIdAndTodoPhase(teamuser.getUserId(), WorkflowTodoDO.TODO_PHASE_TL_APPROVAL);
            todoInMap(todos, todoMap);
        }
    }

    // 查询角色工作流（deptLeader,ops）
    private void gueryRoleTodo(UserDO userDO, HashMap<Long, WorkflowTodoDO> todoMap) {
        if (authService.isRole(userDO.getUsername(), RoleDO.roleDeptLeader)) {
            List<WorkflowTodoDO> todos = workflowDao.queryTodoByTodoPhase(WorkflowTodoDO.TODO_PHASE_DL_APPROVAL);
            todoInMap(todos, todoMap);
        }
        if (authService.isRole(userDO.getUsername(), RoleDO.roleDevOps)) {
            List<WorkflowTodoDO> todos = workflowDao.queryTodoByTodoPhase(WorkflowTodoDO.TODO_PHASE_AUDITING);
            todoInMap(todos, todoMap);
        }
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
    private void todoInMap(List<WorkflowTodoDO> todos, HashMap<Long, WorkflowTodoDO> todoMap) {
        for (WorkflowTodoDO todo : todos)
            todoMap.put(todo.getId(), todo);
    }


}
