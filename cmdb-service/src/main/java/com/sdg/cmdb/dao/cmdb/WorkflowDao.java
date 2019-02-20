package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.workflow.WorkflowDO;
import com.sdg.cmdb.domain.workflow.WorkflowGroupDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoUserDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WorkflowDao {

    List<WorkflowGroupDO> getWorkflowGroup();

    List<WorkflowDO> queryWorkflowByGroupIdAndTopics(@Param("groupId") long groupId, @Param("topics") String topics);

    WorkflowDO getWorkflowByKey(@Param("wfKey") String wfKey);

    WorkflowDO getWorkflow(@Param("id") long id);

    List<WorkflowTodoDetailDO> getTodoDetailByTodoId(@Param("todoId") long todoId);

    /**
     * 工作流查询 workflowTodo
     */
    List<WorkflowTodoDO> queryTodoByApplyUserId(@Param("applyUserId") long applyUserId);

    /**
     * 工作流查询 本人已完成
     * @param applyUserId
     * @param todoPhase
     * @return
     */
    List<WorkflowTodoDO> queryTodoByApplyUserIdAndTodoPhase(@Param("applyUserId") long applyUserId, @Param("todoPhase") int todoPhase);

    List<WorkflowTodoDO> queryTodoByTodoPhase(@Param("todoPhase") int todoPhase);

    WorkflowTodoDO getTodo(@Param("id") long id);

    int addTodo(WorkflowTodoDO workflowTodoDO);

    int delTodo(@Param("id") long id);

    int updateTodo(WorkflowTodoDO workflowTodoDO);


    int addTodoDetail(WorkflowTodoDetailDO workflowTodoDetailDO);

    int delTodoDetail(@Param("id") long id);

    int updateTodoDetail(WorkflowTodoDetailDO workflowTodoDetailDO);

    List<WorkflowTodoUserDO> getTodoUserByTodoId(@Param("todoId") long todoId);

    WorkflowTodoUserDO getTodoUserByTodoIdAndAssigneeType(@Param("todoId") long todoId, @Param("assigneeType") int assigneeType);

    int addTodoUser(WorkflowTodoUserDO workflowTodoUserDO);

    int delTodoUser(@Param("id") long id);

    int updateTodoUser(WorkflowTodoUserDO workflowTodoUserDO);


}
