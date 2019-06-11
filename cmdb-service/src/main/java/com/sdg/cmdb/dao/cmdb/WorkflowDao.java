package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.workflow.WorkflowDO;
import com.sdg.cmdb.domain.workflow.WorkflowGroupDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoUserDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.status.WorkflowTodoMonthStatus;
import com.sdg.cmdb.domain.workflow.status.WorkflowTodoStatus;
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
     *
     * @param applyUserId
     * @param todoPhase
     * @return
     */
    List<WorkflowTodoDO> queryTodoByApplyUserIdAndTodoPhase(@Param("applyUserId") long applyUserId, @Param("todoPhase") int todoPhase);

    List<WorkflowTodoDO> queryTodoByTodoPhase(@Param("todoPhase") int todoPhase);

    /**
     * 查询需要审批的工作流
     *
     * @param todoPhase
     * @param userId    审批人
     * @return
     */
    List<WorkflowTodoDO> queryTodoByApproval(@Param("todoPhase") int todoPhase, @Param("userId") long userId);

    long queryTodoSize(@Param("queryName") String queryName,
                       @Param("queryPhase") int queryPhase);


    int getMyTodoSize(@Param("applyUserId") long applyUserId,
                      @Param("queryPhase") int queryPhase);

    List<WorkflowTodoDO> queryTodoPage(@Param("queryName") String queryName,
                                       @Param("queryPhase") int queryPhase,
                                       @Param("pageStart") long pageStart,
                                       @Param("length") int length);

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

    List<WorkflowTodoMonthStatus> statusTodoByMonth();

    List<WorkflowTodoStatus> statusTodoByWorkflow();

}
