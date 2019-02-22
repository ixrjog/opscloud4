package com.sdg.cmdb.factory.workflow;

import com.sdg.cmdb.dao.cmdb.TeamDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.dao.cmdb.WorkflowDao;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.workflow.*;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailLdapGroup;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.NotificationCenterService;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class TodoAbs implements InitializingBean {

    @Resource
    protected UserDao userDao;

    @Resource
    private WorkflowDao workflowDao;

    @Resource
    private TeamDao teamDao;

    @Resource
    private AuthService authService;

    @Autowired
    private NotificationCenterService ncService;

    abstract public String getKey();

    /**
     * 审批审核
     *
     * @param todoId
     * @return
     */
    public boolean approvalTodo(long todoId) {
        WorkflowTodoVO todoVO = getTodo(todoId);
        // 工单状态
        if (approvalTodo(todoVO)) {
            // TODO 审批成功，重新设置工作流阶段 todoPhase
            HashMap<Integer, WorkflowAppoval> userMap = getWorkflowAppoval(todoVO.getWorkflowDO(), todoVO.getId());
            int todoPhase = todoVO.getTodoPhase() + 1;
            for (int i = todoPhase; i <= 4; i++) {
                WorkflowAppoval wa = userMap.get(i);
                if (!wa.isCheck()) {
                    todoVO.setTodoPhase(i);
                    break;
                }
            }
            // TODO 重新插入审批信息
            todoVO.setTodoUserList(getTodoUserMap(todoVO.getId()));
        }
        if (checkApproval(todoVO)) {
            todoVO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_COMPLETE);
            workflowDao.updateTodo(todoVO);
            ncService.notifWorkflowTodo(todoVO, userDao.getUserById(todoVO.getApplyUserId()));
            return (invokeTodo(todoVO.getId()));
        } else {
            workflowDao.updateTodo(todoVO);
        }
        return true;
    }

    public boolean disapproveTodo(long todoId) {
        WorkflowTodoVO todoVO = getTodo(todoId);
        List<WorkflowTodoUserDO> todoUserList = getTodoUserList(todoId);
        String username = SessionUtils.getUsername();
        try {
            boolean check = false;
            for (WorkflowTodoUserDO todoUser : todoUserList) {
                // TODO 匹配到用户
                if (todoUser.getUsername().equals(username)) {
                    check = true;
                    todoUser.setEvaluation(WorkflowTodoUserDO.EvaluationTypeEnum.disapprove.getCode());
                    workflowDao.updateTodoUser(todoUser);
                }
            }
            if (check) {
                todoVO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_COMPLETE);
                todoVO.setTodoStatus(WorkflowTodoDO.TODO_STATUS_REFUSE);
                workflowDao.updateTodo(todoVO);
                ncService.notifWorkflowTodo(todoVO, userDao.getUserById(todoVO.getApplyUserId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean approvalTodo(WorkflowTodoVO todoVO) {
        int todoPhase = todoVO.getTodoPhase();
        switch (todoPhase) {
            // TODO 申请状态直接跳过
            case WorkflowTodoDO.TODO_PHASE_APPlY:
                return false;
            // TODO 质量审批
            case WorkflowTodoDO.TODO_PHASE_QA_APPROVAL:
                // TODO 审批成功
                return checkApprovalAndUpdate(getTodoUserByAssigneeType(todoVO.getId(), WorkflowTodoUserDO.AssigneeTypeEnum.qc.getCode()));
            case WorkflowTodoDO.TODO_PHASE_TL_APPROVAL:
                return checkApprovalAndUpdate(getTodoUserByAssigneeType(todoVO.getId(), WorkflowTodoUserDO.AssigneeTypeEnum.teamleader.getCode()));
            case WorkflowTodoDO.TODO_PHASE_DL_APPROVAL:
                return checkApprovalAndUpdate(getTodoUserByAssigneeType(todoVO.getId(), WorkflowTodoUserDO.AssigneeTypeEnum.deptLeader.getCode()));
            case WorkflowTodoDO.TODO_PHASE_AUDITING:
                return checkApprovalAndUpdate(getTodoUserByAssigneeType(todoVO.getId(), WorkflowTodoUserDO.AssigneeTypeEnum.ops.getCode()));
        }
        return false;
    }

    /**
     * 返回一个 阶段为key的审核用户map
     *
     * @param workflowDO
     * @param todoId
     * @return
     */
    private HashMap<Integer, WorkflowAppoval> getWorkflowAppoval(WorkflowDO workflowDO, long todoId) {
        HashMap<String, WorkflowTodoUserDO> userMap = getTodoUserMap(todoId);
        HashMap<Integer, WorkflowAppoval> map = new HashMap<>();
        WorkflowAppoval waTl = new WorkflowAppoval(workflowDO.isTlApproval(), userMap.get(WorkflowTodoUserDO.AssigneeTypeEnum.teamleader.getDesc()));
        map.put(WorkflowTodoDO.TODO_PHASE_TL_APPROVAL, waTl);
        WorkflowAppoval waDl = new WorkflowAppoval(workflowDO.isDlApproval(), userMap.get(WorkflowTodoUserDO.AssigneeTypeEnum.deptLeader.getDesc()));
        map.put(WorkflowTodoDO.TODO_PHASE_DL_APPROVAL, waDl);
        WorkflowAppoval waOps = new WorkflowAppoval(workflowDO.isOpsAudit(), userMap.get(WorkflowTodoUserDO.AssigneeTypeEnum.ops.getDesc()));
        map.put(WorkflowTodoDO.TODO_PHASE_AUDITING, waOps);
        return map;
    }

    // 校验用户并更新状态
    private boolean checkApprovalAndUpdate(WorkflowTodoUserDO todoUser) {
        // 没有条目 证明不需要审批
        if (todoUser.getId() == 0) return true;
        if (SessionUtils.getUsername().equals(todoUser.getUsername())) {
            try {
                todoUser.setEvaluation(WorkflowTodoUserDO.EvaluationTypeEnum.approve.getCode());
                workflowDao.updateTodoUser(todoUser);
            } catch (Exception e) {

            }
            return true;
        }
        return false;
    }

    /**
     * 按角色类型 取出工作流中的 审批用户
     *
     * @param todoId
     * @param assigneeType
     * @return
     */
    private WorkflowTodoUserDO getTodoUserByAssigneeType(long todoId, int assigneeType) {
        List<WorkflowTodoUserDO> todoUsers = getTodoUserList(todoId);
        for (WorkflowTodoUserDO todoUser : todoUsers)
            if (todoUser.getAssigneeType() == assigneeType)
                return todoUser;
        return new WorkflowTodoUserDO();
    }

    /**
     * 新建workflowTodo
     *
     * @return
     */
    public WorkflowTodoVO createTodo() {
        WorkflowTodoDO workflowTodoDO = buildWorkflowTodo();
        workflowDao.addTodo(workflowTodoDO);
        long todoId = workflowTodoDO.getId();
        createTodoUser(workflowTodoDO);
        WorkflowTodoVO workflowTodoVO = new WorkflowTodoVO(getWorkflow(), workflowTodoDO, getWorkflowTodoDetailVOList(todoId), getTodoUserMap(todoId));
        invokeTodoUserList(workflowTodoVO);
        return workflowTodoVO;
    }

    /**
     * 插入teamleader
     */
    private void createTodoUser(WorkflowTodoDO workflowTodoDO) {
        //UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        UserDO teamleaderUserDO = getTeamleader();
        if (teamleaderUserDO == null || StringUtils.isEmpty(teamleaderUserDO.getUsername())) return;
        WorkflowTodoUserDO teamleader = new WorkflowTodoUserDO(workflowTodoDO.getId(), teamleaderUserDO, WorkflowTodoUserDO.AssigneeTypeEnum.teamleader.getCode());
        workflowDao.addTodoUser(teamleader);
    }

    /**
     * 获取当前用户的Teamleader
     *
     * @return
     */
    private UserDO getTeamleader() {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        TeamuserDO teamuserDO = teamDao.queryTeamuserByUserId(userDO.getId());
        TeamDO teamDO;
        if (teamuserDO != null) {
            teamDO = teamDao.getTeam(teamuserDO.getTeamId());
        } else {
            // TODO 判断用户是否为TL
            teamDO = teamDao.queryTeamByLeaderUserId(userDO.getId());
        }
        if (teamDO != null)
            return userDao.getUserById(teamDO.getTeamleaderUserId());
        return new UserDO();
    }

    abstract public WorkflowTodoVO saveTodo(WorkflowTodoVO workflowTodoVO);


    abstract protected Type getType();

    /**
     * 查询一个Todo
     *
     * @param todoId
     * @return
     */
    public WorkflowTodoVO getTodo(long todoId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowDO workflowDO = getWorkflow();
        WorkflowTodoVO workflowTodoVO = new WorkflowTodoVO(workflowDO, workflowTodoDO, getWorkflowTodoDetailVOList(todoId), getTodoUserMap(todoId));
        workflowTodoVO.setApplyViewTime(TimeViewUtils.format(workflowTodoVO.getGmtApply()));
        // TODO 未提交的工作流插入审批人选择列表
        invokeTodoUserList(workflowTodoVO);
        return workflowTodoVO;
    }

    /**
     * 未提交的工作流插入审批人选择列表
     *
     * @param workflowTodoVO
     */
    private void invokeTodoUserList(WorkflowTodoVO workflowTodoVO) {
        WorkflowDO workflowDO = workflowTodoVO.getWorkflowDO();
        // 全局都需要用户列表 workflowTodoVO.getTodoPhase() == WorkflowTodoDO.TODO_PHASE_APPlY &&
        if (workflowDO.isApproval()) {
            if (workflowDO.isQaApproval())
                workflowTodoVO.setQaUserList(getUserByRole(RoleDO.roleQualityAssurance));
            if (workflowDO.isDlApproval())
                workflowTodoVO.setDlUserList(getUserByRole(RoleDO.roleDeptLeader));
            if (workflowDO.isOpsAudit())
                workflowTodoVO.setOpsUserList(getUserByRole(RoleDO.roleDevOps));
        }
    }

    private List<UserVO> getUserByRole(String roleName) {
        return authService.getUsersByRole(roleName);
    }

    /**
     * 保存 审批/审核 用户信息
     *
     * @param workflowTodoVO
     */
    protected void saveTodoUser(WorkflowTodoVO workflowTodoVO) {
        HashMap<String, WorkflowTodoUserDO> todoUserList = workflowTodoVO.getTodoUserList();
        for (String key : todoUserList.keySet()) {
            WorkflowTodoUserDO user = todoUserList.get(key);
            user.setTodoId(workflowTodoVO.getId());
            saveTodoUser(key, user);
        }

    }

    private boolean saveTodoUser(String assigneeDesc, WorkflowTodoUserDO todoUser) {
        WorkflowTodoUserDO user = workflowDao.getTodoUserByTodoIdAndAssigneeType(todoUser.getTodoId(), WorkflowTodoUserDO.AssigneeTypeEnum.valueOf(assigneeDesc).getCode());
        try {
            UserDO userDO = userDao.getUserById(todoUser.getUserId());
            if (user == null) {
                todoUser = new WorkflowTodoUserDO(todoUser.getTodoId(), userDO, WorkflowTodoUserDO.AssigneeTypeEnum.valueOf(assigneeDesc).getCode());
                workflowDao.addTodoUser(todoUser);
            }else{
                user.setUserId(userDO.getId());
                user.setUsername(user.getUsername());
                user.setDisplayName(user.getDisplayName());
                workflowDao.updateTodoUser(user);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        // TODO 前置检查
        if (!checkTodoUser(workflowDO, workflowTodoDO.getId()))
            return getTodo(workflowTodoDO.getId());
        workflowTodoDO.setTodoPhase(getTodoPhase(workflowDO));
        try {
            // TODO 设置申请时间
            workflowTodoDO.setGmtApply(TimeUtils.nowDate());
            // TODO 设置工作流详情
            workflowDao.updateTodo(workflowTodoDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WorkflowTodoVO workflowTodoVO = getTodo(workflowTodoDO.getId());
        UserDO userDO = userDao.getUserById(getUserByTodoPhase(workflowTodoVO).getUserId());
        ncService.notifWorkflowTodo(workflowTodoVO, userDO);
        return workflowTodoVO;
    }

    private WorkflowTodoUserDO getUserByTodoPhase(WorkflowTodoVO workflowTodoVO) {
        int todoPhase = workflowTodoVO.getTodoPhase();
        HashMap<String, WorkflowTodoUserDO> todoUserList = workflowTodoVO.getTodoUserList();
        if (todoPhase == WorkflowTodoDO.TODO_PHASE_TL_APPROVAL)
            return todoUserList.get(WorkflowTodoUserDO.AssigneeTypeEnum.teamleader.getDesc());
        if (todoPhase == WorkflowTodoDO.TODO_PHASE_DL_APPROVAL)
            return todoUserList.get(WorkflowTodoUserDO.AssigneeTypeEnum.deptLeader.getDesc());
        if (todoPhase == WorkflowTodoDO.TODO_PHASE_AUDITING)
            return todoUserList.get(WorkflowTodoUserDO.AssigneeTypeEnum.ops.getDesc());
        return new WorkflowTodoUserDO();
    }

    private boolean checkTodoUser(WorkflowDO workflowDO, long todoId) {
        if (!workflowDO.isApproval())
            return true;
        HashMap<String, WorkflowTodoUserDO> map = getTodoUserMap(todoId);
        if (workflowDO.isTlApproval()) {
            if (!map.containsKey(WorkflowTodoUserDO.AssigneeTypeEnum.teamleader.getDesc()))
                return false;
        }
        if (workflowDO.isDlApproval()) {
            if (!map.containsKey(WorkflowTodoUserDO.AssigneeTypeEnum.deptLeader.getDesc()))
                return false;
        }
        if (workflowDO.isOpsAudit()) {
            if (!map.containsKey(WorkflowTodoUserDO.AssigneeTypeEnum.ops.getDesc()))
                return false;
        }
        return true;
    }

    /**
     * 新提交工单生成 工单阶段(如工单无需审批则设置为审核，还需做一次判断，无需审核直接执行)
     *
     * @param workflowDO
     * @return
     */
    private int getTodoPhase(WorkflowDO workflowDO) {
        if (workflowDO.isApproval()) {
            if (workflowDO.isQaApproval())
                return WorkflowTodoDO.TODO_PHASE_QA_APPROVAL;
            if (workflowDO.isTlApproval())
                return WorkflowTodoDO.TODO_PHASE_TL_APPROVAL;
            if (workflowDO.isDlApproval())
                return WorkflowTodoDO.TODO_PHASE_DL_APPROVAL;
        }
        return WorkflowTodoDO.TODO_PHASE_AUDITING;
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
        // TODO 如果工单没有细节，看是否需要生成初始信息
        if (doList.size() == 0)
            createTodoDetails(todoId, voList);
        for (WorkflowTodoDetailDO workflowTodoDetailDO : doList) {
            WorkflowTodoDetailVO workflowTodoDetailVO = getTodoDetailVO(workflowTodoDetailDO);
            voList.add(workflowTodoDetailVO);

        }
        return voList;
    }

    /**
     * 去重
     */
    protected void removeTodoDetails(long todoId, List<WorkflowTodoDetailVO> todoDetails) {
        List<WorkflowTodoDetailVO> list = getWorkflowTodoDetailVOList(todoId);
        HashMap<String, WorkflowTodoDetailVO> map = new HashMap<>();
        for (WorkflowTodoDetailVO todoDetail : todoDetails)
            map.put(todoDetail.getName(), todoDetail);

        for (WorkflowTodoDetailVO todoDetail : list) {
            if (!map.containsKey(todoDetail.getName()))
                delTodoDetail(todoId, todoDetail.getId());


        }
    }

    /**
     * 新工作流创建初始信息
     *
     * @param todoId
     * @param detailList
     */
    abstract protected void createTodoDetails(long todoId, List<WorkflowTodoDetailVO> detailList);

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


    /**
     * 执行工单
     *
     * @param todoId
     * @return
     */
    public boolean invokeTodo(long todoId) {
        WorkflowTodoVO todoVO = getTodo(todoId);
        // 校验审批流程是否完成
        if (!checkApproval(todoVO))
            return false;
        boolean result = invokeTodoDetails(todoVO);
        // 写入步骤
        todoVO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_COMPLETE);
        // 写入状态
        if (result) {
            todoVO.setTodoStatus(WorkflowTodoDO.TODO_STATUS_COMPLETE);
        } else {
            todoVO.setTodoStatus(WorkflowTodoDO.TODO_STATUS_ERR);
        }
        updateTodo(todoVO);
        return result;
    }

    /**
     * 执行工单细节
     *
     * @param todoVO
     * @return
     */
    abstract protected boolean invokeTodoDetails(WorkflowTodoVO todoVO);

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkflowTodoFactory.register(this);
    }

}
