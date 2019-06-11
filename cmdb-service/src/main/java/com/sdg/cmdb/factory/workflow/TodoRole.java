package com.sdg.cmdb.factory.workflow;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;

import com.sdg.cmdb.domain.workflow.detail.TodoDetailRole;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import com.sdg.cmdb.service.AuthService;

import com.sdg.cmdb.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class TodoRole extends TodoAbs implements Serializable {

    private static final long serialVersionUID = -7794573587429912630L;
    private final String TODO_KEY = "ROLE";

    @Autowired
    private AuthService authService;

    @Override
    protected Type getType() {
        return TodoDetailRole.class;
    }

    /**
     * 工作流名称
     *
     * @return
     */
    @Override
    public String getKey() {
        return TODO_KEY;
    }


    /**
     * 暂存
     *
     * @param workflowTodoVO
     * @return
     */
    @Override
    public WorkflowTodoVO saveTodo(WorkflowTodoVO workflowTodoVO) {
        List<WorkflowTodoDetailVO> todoDetails = workflowTodoVO.getTodoDetails();
        String notice = "OC平台角色: ";
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            WorkflowTodoDetailDO workflowTodoDetailDO = getTodoDetailDO(workflowTodoDetailVO);
            if (!saveTodoDetail(workflowTodoDetailDO)) return workflowTodoVO;
            // TODO 插入工作流详情
            Gson gson = new GsonBuilder().create();
            TodoDetailRole todoDetail = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
            if (todoDetail.isApply())
                notice += workflowTodoDetailVO.getName() + ";";
        }
        workflowTodoVO.setNotice(notice);
        saveTodoUser(workflowTodoVO);
        updateTodo(workflowTodoVO);
        return getTodo(workflowTodoVO.getId());
    }

    /**
     * 封装对象
     *
     * @param todoDetailDO
     * @return
     */
    protected WorkflowTodoDetailVO getTodoDetailVO(WorkflowTodoDetailDO todoDetailDO) {
        // TODO 对象转json
        Gson gson = new GsonBuilder().create();
        TodoDetailRole todoDetailRole = gson.fromJson(todoDetailDO.getDetailValue(), getType());
        return new WorkflowTodoDetailVO(todoDetailRole, todoDetailDO);
    }


    private WorkflowTodoDetailDO getTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO) {
        Gson gson = new GsonBuilder().create();
        TodoDetailRole todoDetailRole = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
        WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(workflowTodoDetailVO, todoDetailRole.toString());
        return workflowTodoDetailDO;
    }

    /**
     * 新建工作流初始化
     *
     * @param todoId
     * @param detailList
     */
    @Override
    protected void createTodoDetails(long todoId, List<WorkflowTodoDetailVO> detailList) {
        List<TodoDetailRole> roleList = authService.getUserWorkflowRole(SessionUtils.getUsername());
        for (TodoDetailRole todoDetailRole : roleList) {
            WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(todoId, todoDetailRole.getRoleDO().getRoleName(), getKey());
            WorkflowTodoDetailVO workflowTodoDetailVO = new WorkflowTodoDetailVO(todoDetailRole, workflowTodoDetailDO);
            detailList.add(workflowTodoDetailVO);
        }
    }

    @Override
    protected boolean invokeTodoDetails(WorkflowTodoVO todoVO) {
        List<WorkflowTodoDetailVO> todoDetails = todoVO.getTodoDetails();
        UserDO userDO = userDao.getUserById(todoVO.getApplyUserId());
        boolean result = true;
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            TodoDetailRole todoDetailRole = (TodoDetailRole) workflowTodoDetailVO.getDetail();
            // TODO 判断是否申请
            if (!todoDetailRole .isApply())
                continue;
            if(authService.userRoleBind(todoDetailRole.getRoleDO().getId(),userDO.getUsername()).isSuccess()){
                workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_COMPLETE);
            } else {
                workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_ERR);
                result = false;
            }
            saveTodoDetail(workflowTodoDetailVO);
        }
        return result;
    }
}
