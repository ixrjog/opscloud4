package com.sdg.cmdb.factory.workflow;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.ci.CiAppAuthDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailCITestAuth;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import com.sdg.cmdb.service.CiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class TodoCITestAuth extends TodoAbs {

    private final String TODO_KEY = "CITESTAUTH";

    @Autowired
    private CiService ciService;

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
        String notice = "持续集成Android应用: ";
        // 删除所有工单详情，此工单只允许一条详情
        delTodoDetails(workflowTodoVO.getId());
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            WorkflowTodoDetailDO workflowTodoDetailDO = getTodoDetailDO(workflowTodoDetailVO);
            TodoDetailCITestAuth todoDetailCITestAuth = (TodoDetailCITestAuth) getTodoDetailVO(workflowTodoDetailDO).getDetail();
            workflowTodoDetailDO.setName(todoDetailCITestAuth.getAppName());
            if (!saveTodoDetail(workflowTodoDetailDO))
                return workflowTodoVO;
            notice += workflowTodoDetailDO.getName() + ";";
            // 只允许处理一条数据，群组审批原因
            break;
        }
        workflowTodoVO.setNotice(notice);
        saveTodoUser(workflowTodoVO);
        updateTodo(workflowTodoVO);
        return getTodo(workflowTodoVO.getId());
    }

    @Override
    protected Type getType() {
        return TodoDetailCITestAuth.class;
    }

    protected WorkflowTodoDetailVO getTodoDetailVO(WorkflowTodoDetailDO todoDetailDO) {
        // 对象转json
        Gson gson = new GsonBuilder().create();
        TodoDetailCITestAuth todoDetailCITestAuth = gson.fromJson(todoDetailDO.getDetailValue(), getType());
        return new WorkflowTodoDetailVO(todoDetailCITestAuth, todoDetailDO);
    }

    private WorkflowTodoDetailDO getTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO) {
        Gson gson = new GsonBuilder().create();
        TodoDetailCITestAuth todoDetailCITestAuth = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
        WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(workflowTodoDetailVO, todoDetailCITestAuth.toString());
        return workflowTodoDetailDO;
    }

    @Override
    protected boolean invokeTodoDetails(WorkflowTodoVO todoVO) {
        List<WorkflowTodoDetailVO> todoDetails = todoVO.getTodoDetails();
        UserDO userDO = userDao.getUserById(todoVO.getApplyUserId());
        boolean result = true;
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            TodoDetailCITestAuth ciTestAuth = (TodoDetailCITestAuth) workflowTodoDetailVO.getDetail();
            CiAppAuthDO ciAppAuthDO = new CiAppAuthDO(ciTestAuth.getAppId(),ciTestAuth.getAppName(),userDO);
            if (ciService.ciAppAuth(ciAppAuthDO)) {
                workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_COMPLETE);
            } else {
                workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_ERR);
                result = false;
            }
            saveTodoDetail(workflowTodoDetailVO);
            break; // 只允许执行一条
        }

        return result;
    }
}
