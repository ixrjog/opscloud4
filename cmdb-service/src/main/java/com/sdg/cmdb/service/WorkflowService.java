package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.workflow.WorkflowGroupVO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;

import java.util.List;

public interface WorkflowService {

    List<WorkflowGroupVO> queryWorkflowGroup(String topics);

    WorkflowTodoVO createTodo(String wfKey);

    WorkflowTodoVO saveTodo(WorkflowTodoVO workflowTodoVO);

    /**
     * 删除工作流
     * @param todoId
     * @return
     */
    BusinessWrapper<Boolean> revokeTodo(long todoId);


    /**
     * 执行工作流任务
     * @param todoId
     * @return
     */
    BusinessWrapper<Boolean> invokeTodo(long todoId);


    /**
     * 审批工单
     * @param todoId
     * @return
     */
    BusinessWrapper<Boolean> approvalTodo(long todoId);


    WorkflowTodoVO delTodoDetail(long todoId,long detailId);

    /**
     * 申请
     * @param todoId
     * @return
     */
    WorkflowTodoVO applyTodo(long todoId);

    /**
     * 查询待办工作
     * @return
     */
    List<WorkflowTodoVO> queryMyTodo();

}
