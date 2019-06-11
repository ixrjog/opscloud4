package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.workflow.WorkflowGroupVO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.status.WorkflowStatus;

import java.util.List;

public interface WorkflowService {

    List<WorkflowGroupVO> queryWorkflowGroup(String topics);

    /**
     * 创建工作流
     * @param wfKey
     * @return
     */
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
     * 审批同意
     * @param todoId
     * @return
     */
    BusinessWrapper<Boolean> approvalTodo(long todoId);

    /**
     * 不批准
     * @param todoId
     * @return
     */
    BusinessWrapper<Boolean> disapproveTodo(long todoId);



    WorkflowTodoVO delTodoDetail(long todoId,long detailId);

    /**
     * 测试用
     * @param todoId
     * @return
     */
    WorkflowTodoVO getTodo(long todoId);

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

    /**
     * 查询完成工作
     * @return
     */
    List<WorkflowTodoVO> queryMyCompleteTodo();

    TableVO<List<WorkflowTodoVO>> getWorkflowTodoPage(String queryName, int queryPhase, int page, int length);

    int getMyTodoSize(int queryPhase);

    WorkflowStatus getWorkflowStatus();

}
