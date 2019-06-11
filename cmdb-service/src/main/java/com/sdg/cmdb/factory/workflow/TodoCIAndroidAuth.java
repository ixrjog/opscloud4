package com.sdg.cmdb.factory.workflow;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.ci.CiAppAuthDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.detail.*;
import com.sdg.cmdb.service.CiService;
import com.sdg.cmdb.service.GitlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;


@Service
public class TodoCIAndroidAuth extends TodoAbs {

    private final String TODO_KEY = "CIANDROIDAUTH";

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
        String notice = "持续集成自动化测试应用: ";
        // 删除所有工单详情，此工单只允许一条详情
        delTodoDetails(workflowTodoVO.getId());
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            WorkflowTodoDetailDO workflowTodoDetailDO = getTodoDetailDO(workflowTodoDetailVO);
            TodoDetailCIAndroidAuth todoDetailCIAndroidAuth = (TodoDetailCIAndroidAuth) getTodoDetailVO(workflowTodoDetailDO).getDetail();
            workflowTodoDetailDO.setName(todoDetailCIAndroidAuth.getAppName());
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
        return TodoDetailCIAndroidAuth.class;
    }

    protected WorkflowTodoDetailVO getTodoDetailVO(WorkflowTodoDetailDO todoDetailDO) {
        // 对象转json
        Gson gson = new GsonBuilder().create();
        TodoDetailCIAndroidAuth todoDetailCIAndroidAuth = gson.fromJson(todoDetailDO.getDetailValue(), getType());
        return new WorkflowTodoDetailVO(todoDetailCIAndroidAuth, todoDetailDO);
    }

    private WorkflowTodoDetailDO getTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO) {
        Gson gson = new GsonBuilder().create();
        TodoDetailCIAndroidAuth todoDetailCIAndroidAuth = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
        WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(workflowTodoDetailVO, todoDetailCIAndroidAuth.toString());
        return workflowTodoDetailDO;
    }

    @Override
    protected boolean invokeTodoDetails(WorkflowTodoVO todoVO) {
        List<WorkflowTodoDetailVO> todoDetails = todoVO.getTodoDetails();
        UserDO userDO = userDao.getUserById(todoVO.getApplyUserId());
        boolean result = true;
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            TodoDetailCIAndroidAuth ciAndroidAuth = (TodoDetailCIAndroidAuth) workflowTodoDetailVO.getDetail();
            CiAppAuthDO ciAppAuthDO = new CiAppAuthDO(ciAndroidAuth.getAppId(),ciAndroidAuth.getAppName(),userDO);
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
