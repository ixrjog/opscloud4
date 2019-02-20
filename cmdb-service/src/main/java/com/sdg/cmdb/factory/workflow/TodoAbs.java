package com.sdg.cmdb.factory.workflow;

import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.dao.cmdb.WorkflowDao;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.WorkflowDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoUserDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class TodoAbs implements InitializingBean {

    @Resource
    protected UserDao userDao;

    @Resource
    private WorkflowDao workflowDao;

    abstract public String getKey();

    /**
     * 审批审核
     *
     * @param todoId
     * @return
     */
    public boolean approvalTodo(long todoId) {
        WorkflowTodoVO todoVO = getTodo(todoId);
        WorkflowDO workflowDO = todoVO.getWorkflowDO();
        // 工单状态
        int todoPhase = todoVO.getTodoPhase();
        switch (todoPhase) {
            // TODO 申请状态直接跳过
            case WorkflowTodoDO.TODO_PHASE_APPlY:
                return false;
            // TODO 质量审批
            case WorkflowTodoDO.TODO_PHASE_QA_APPROVAL:
                if(workflowDO.isQaApproval()){

                }
                break;
        }

        return true;

    }


    /**
     * 新建todo
     *
     * @return
     */
    public WorkflowTodoVO createTodo() {
        WorkflowTodoDO workflowTodoDO = buildWorkflowTodo();
        workflowDao.addTodo(workflowTodoDO);
        long todoId = workflowTodoDO.getId();
        WorkflowTodoVO workflowTodoVO = new WorkflowTodoVO(getWorkflow(), workflowTodoDO, getWorkflowTodoDetailVOList(todoId), getTodoUserMap(todoId));
        return workflowTodoVO;
    }

    abstract public WorkflowTodoVO saveTodo(WorkflowTodoVO workflowTodoVO);

    // abstract boolean saveTodo(long todoId);

    abstract public boolean invokeTodo(long todoId);


    /**
     * 查询一个Todo
     *
     * @param todoId
     * @return
     */
    public WorkflowTodoVO getTodo(long todoId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowTodoVO workflowTodoVO = new WorkflowTodoVO(getWorkflow(), workflowTodoDO, getWorkflowTodoDetailVOList(todoId), getTodoUserMap(todoId));
        workflowTodoVO.setApplyViewTime(TimeViewUtils.format(workflowTodoVO.getGmtApply()));
        return workflowTodoVO;
    }


    abstract protected WorkflowTodoDetailVO getTodoDetailVO(WorkflowTodoDetailDO workflowTodoDetailDO);

    /**
     * 校验审批 内部调用
     *
     * @param todoVO
     * @return
     */
    protected boolean checkApproval(WorkflowTodoVO todoVO) {
        WorkflowDO workflowDO = todoVO.getWorkflowDO();
        HashMap<String, WorkflowTodoUserDO> userMap = todoVO.getTodoUserList();
        // check approval 是否需要审批
        if (!workflowDO.isApproval()) return true;
        // check qaApproval QA审批
        if (workflowDO.isQaApproval())
            if (!checkApprovalByUser(userMap.get(WorkflowTodoUserDO.AssigneeTypeEnum.qc.getDesc())))
                return false;
        // check tlApproval TL审批
        if (workflowDO.isTlApproval())
            if (!checkApprovalByUser(userMap.get(WorkflowTodoUserDO.AssigneeTypeEnum.teamleader.getDesc())))
                return false;
        // check dlApproval DL审批
        if (workflowDO.isDlApproval())
            if (!checkApprovalByUser(userMap.get(WorkflowTodoUserDO.AssigneeTypeEnum.deptLeader.getDesc())))
                return false;
        // check opsAudit OPS审核
        if (workflowDO.isOpsAudit())
            if (!checkApprovalByUser(userMap.get(WorkflowTodoUserDO.AssigneeTypeEnum.ops.getDesc())))
                return false;
        return true;
    }

    private boolean checkApprovalByUser(WorkflowTodoUserDO todoUserDO) {
        return todoUserDO.getEvaluation() == WorkflowTodoUserDO.EvaluationTypeEnum.approve.getCode();
    }

    private boolean checkTodoPhase(WorkflowTodoDO todoDO) {
        if (todoDO.getTodoPhase() == WorkflowTodoDO.TODO_PHASE_APPlY)
            return true;
        return false;
    }

    private boolean checkTodoPhase(long todoId) {
        return checkTodoPhase(workflowDao.getTodo(todoId));
    }

    /**
     * 删除工单细节
     *
     * @return
     */
    public WorkflowTodoVO delTodoDetail(long todoId, long detailId) {
        if (checkTodoPhase(todoId)) {
            try {
                workflowDao.delTodoDetail(detailId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getTodo(todoId);
    }

    /**
     * 申请
     *
     * @param workflowDO
     * @param workflowTodoDO
     * @return
     */
    public WorkflowTodoVO applyTodo(WorkflowDO workflowDO, WorkflowTodoDO workflowTodoDO) {
        // 是否审批
        if (workflowDO.isApproval()) {
            if (workflowDO.isQaApproval())
                workflowTodoDO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_QA_APPROVAL);
            if (workflowDO.isTlApproval())
                workflowTodoDO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_TL_APPROVAL);
            if (workflowDO.isDlApproval())
                workflowTodoDO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_DL_APPROVAL);
        } else {
            workflowTodoDO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_AUDITING);
        }
        try {
            workflowTodoDO.setGmtApply(TimeUtils.nowDate());
            workflowDao.updateTodo(workflowTodoDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getTodo(workflowTodoDO.getId());
    }

    /**
     * 取工作流细节详情
     *
     * @param todoId
     * @return
     */
    private List<WorkflowTodoDetailVO> getWorkflowTodoDetailVOList(long todoId) {
        List<WorkflowTodoDetailDO> doList = getTodoDetails(todoId);

        List<WorkflowTodoDetailVO> voList = new ArrayList<>();
        for (WorkflowTodoDetailDO workflowTodoDetailDO : doList) {
            WorkflowTodoDetailVO workflowTodoDetailVO = getTodoDetailVO(workflowTodoDetailDO);
            voList.add(workflowTodoDetailVO);

        }
        return voList;
    }

    private HashMap<String, WorkflowTodoUserDO> getTodoUserMap(long todoId) {
        List<WorkflowTodoUserDO> list = getTodoUserList(todoId);
        HashMap<String, WorkflowTodoUserDO> map = new HashMap<String, WorkflowTodoUserDO>();
        for (WorkflowTodoUserDO workflowTodoUserDO : list) {
            map.put(workflowTodoUserDO.getAssigneeDesc(), workflowTodoUserDO);
        }
        return map;
    }

    private List<WorkflowTodoUserDO> getTodoUserList(long todoId) {
        return workflowDao.getTodoUserByTodoId(todoId);
    }


    /**
     * 获取创建工作流 用户
     *
     * @return
     */
    private UserDO getApplyUser() {
        String username = SessionUtils.getUsername();
        if (StringUtils.isEmpty(username))
            username = "admin";
        return userDao.getUserByName(username);
    }

    private WorkflowDO getWorkflow() {
        return workflowDao.getWorkflowByKey(getKey());
    }

    private List<WorkflowTodoDetailDO> getTodoDetails(long todoId) {
        List<WorkflowTodoDetailDO> detailDOList = workflowDao.getTodoDetailByTodoId(todoId);
        return detailDOList;
    }

    protected WorkflowTodoDO buildWorkflowTodo() {
        WorkflowTodoDO workflowTodoDO = new WorkflowTodoDO(getWorkflow(), getApplyUser());
        return workflowTodoDO;
    }

    protected boolean saveTodoDetail(WorkflowTodoDetailDO workflowTodoDetailDO) {
        if (checkTodoPhase(workflowTodoDetailDO.getTodoId())) {
            try {
                if (workflowTodoDetailDO.getId() == 0) {
                    workflowDao.addTodoDetail(workflowTodoDetailDO);
                } else {
                    workflowDao.updateTodoDetail(workflowTodoDetailDO);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    protected boolean updateTodo(WorkflowTodoVO workflowTodoVO) {
        try {
            WorkflowTodoDO workflowTodoDO = new WorkflowTodoDO(workflowTodoVO);
            workflowDao.updateTodo(workflowTodoDO);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkflowTodoFactory.register(this);

    }

}
