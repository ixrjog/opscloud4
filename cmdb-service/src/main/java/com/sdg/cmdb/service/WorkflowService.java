package com.sdg.cmdb.service;


import com.sdg.cmdb.domain.workflow.WorkflowGroupVO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;

import java.util.List;

public interface WorkflowService {

    List<WorkflowGroupVO> queryWorkflowGroup(String topics);

    WorkflowTodoVO createTodo(String wfKey);

    WorkflowTodoVO saveTodo(WorkflowTodoVO workflowTodoVO);

    WorkflowTodoVO delTodoDetail(long todoId,long detailId);


    WorkflowTodoVO applyTodo(long todoId);

}
