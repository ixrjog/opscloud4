package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.workflow.WorkflowGroupVO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;

import java.util.List;

public interface WorkflowService {

    List<WorkflowGroupVO> queryWorkflowGroup(String topics);

<<<<<<< HEAD
=======
    /**
     * 创建工作流
     * @param wfKey
     * @return
     */
>>>>>>> develop
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
<<<<<<< HEAD
     * 审批工单
=======
     * 审批同意
>>>>>>> develop
     * @param todoId
     * @return
     */
    BusinessWrapper<Boolean> approvalTodo(long todoId);

<<<<<<< HEAD
=======
    /**
     * 不批准
     * @param todoId
     * @return
     */
    BusinessWrapper<Boolean> disapproveTodo(long todoId);


>>>>>>> develop

    WorkflowTodoVO delTodoDetail(long todoId,long detailId);

    /**
<<<<<<< HEAD
=======
     * 测试用
     * @param todoId
     * @return
     */
    WorkflowTodoVO getTodo(long todoId);

    /**
>>>>>>> develop
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

<<<<<<< HEAD
=======
    /**
     * 查询完成工作
     * @return
     */
    List<WorkflowTodoVO> queryMyCompleteTodo();

>>>>>>> develop
}
