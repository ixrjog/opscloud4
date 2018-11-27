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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class TodoAbs implements InitializingBean {

    @Resource
    private UserDao userDao;

    @Resource
    private WorkflowDao workflowDao;

    abstract public String getKey();

    /**
     * 新建todo
     * @return
     */
    public WorkflowTodoVO createTodo(){
        WorkflowTodoDO workflowTodoDO = buildWorkflowTodo();
        workflowDao.addTodo(workflowTodoDO);
        long todoId= workflowTodoDO.getId();
        WorkflowTodoVO workflowTodoVO = new WorkflowTodoVO(getWorkflow(),workflowTodoDO,getWorkflowTodoDetailVOList(todoId),getTodoUserMap(todoId));
        return workflowTodoVO;
    }


    /**
     * 申请todo (提交)
     * @param todoId
     * @return
     */
    abstract boolean applyTodo(long todoId);

    abstract public WorkflowTodoVO saveTodo(WorkflowTodoVO workflowTodoVO);

    public WorkflowTodoVO getTodo(long todoId) {
        WorkflowTodoDO workflowTodoDO = workflowDao.getTodo(todoId);
        WorkflowTodoVO workflowTodoVO = new WorkflowTodoVO(getWorkflow(),workflowTodoDO, getWorkflowTodoDetailVOList(todoId), getTodoUserMap(todoId));
        return workflowTodoVO;
    }

    abstract protected WorkflowTodoDetailVO getTodoDetailVO(WorkflowTodoDetailDO workflowTodoDetailDO);

    /**
     * 删除工单细节
     * @return
     */
    public WorkflowTodoVO delTodoDetail(long todoId,long detailId){
        try{
            workflowDao.delTodoDetail(detailId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return getTodo(todoId);
    }

    public WorkflowTodoVO  applyTodo(WorkflowDO workflowDO,WorkflowTodoDO workflowTodoDO){
        // 是否审批
        if(workflowDO.isApproval()){
            workflowTodoDO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_TL_APPROVAL);
        }else{
            workflowTodoDO.setTodoPhase(WorkflowTodoDO.TODO_PHASE_AUDITING);
        }
        try{
            workflowDao.updateTodo(workflowTodoDO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return getTodo(workflowTodoDO.getId());
    }

    /**
     * 取工作流细节详情
     * @param todoId
     * @return
     */
    private List<WorkflowTodoDetailVO> getWorkflowTodoDetailVOList(long todoId){
        List<WorkflowTodoDetailDO> doList = getTodoDetails(todoId);

        List<WorkflowTodoDetailVO> voList = new ArrayList<>();
        for (WorkflowTodoDetailDO workflowTodoDetailDO : doList) {
            WorkflowTodoDetailVO workflowTodoDetailVO = getTodoDetailVO(workflowTodoDetailDO);
            voList.add(workflowTodoDetailVO);

        }
        return voList;
    }

    private HashMap<String,WorkflowTodoUserDO> getTodoUserMap(long todoId){
        List<WorkflowTodoUserDO> list = getTodoUserList(todoId);
        HashMap<String,WorkflowTodoUserDO> map = new HashMap<String,WorkflowTodoUserDO>();
        for(WorkflowTodoUserDO workflowTodoUserDO:list){
            map.put(workflowTodoUserDO.getAssigneeDesc(),workflowTodoUserDO);
        }
        return map;
    }

    private List<WorkflowTodoUserDO> getTodoUserList(long todoId){
        return  workflowDao.getTodoUserByTodoId(todoId);
    }


    /**
     * 获取创建工作流 用户
     *
     * @return
     */
    private UserDO getApplyUser() {
        String username = SessionUtils.getUsername();
        if(StringUtils.isEmpty(username))
            username= "admin";
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

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkflowTodoFactory.register(this);

    }

}
