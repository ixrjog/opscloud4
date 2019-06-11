package com.sdg.cmdb.factory.workflow;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.detail.TodoDetailRAMPolicy;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailDO;
import com.sdg.cmdb.domain.workflow.detail.WorkflowTodoDetailVO;
import com.sdg.cmdb.service.AliyunRAMService;
import com.sdg.cmdb.util.SessionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 阿里云RAMPolicy申请工作流
 */
@Service
public class TodoAliyunRAMPolicy extends TodoAbs implements Serializable {


    private static final long serialVersionUID = -886293503738687412L;
    private final String TODO_KEY = "RAMPolicy";

    @Resource
    private AliyunRAMService aliyunRAMService;

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
        String notice = "RAMPolicy: ";
        // TODO 去重
        removeTodoDetails(workflowTodoVO.getId(), todoDetails);
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            WorkflowTodoDetailDO workflowTodoDetailDO = getTodoDetailDO(workflowTodoDetailVO);
            if (!saveTodoDetail(workflowTodoDetailDO)) return workflowTodoVO;
            // TODO 插入工作流详情
            Gson gson = new GsonBuilder().create();
            TodoDetailRAMPolicy ramPolicy = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
            if (ramPolicy.getDetailType() == 0) {
                // TODO 用户当前的策略
                if (ramPolicy.isDetachPolicy())
                    notice += "撤销授权(" + ramPolicy.getPolicyName() + ");";
            } else {
                // TODO 用户申请的策略
                if (ramPolicy.isAttachPolicy())
                    notice += "附加授权(" + ramPolicy.getPolicyName() + ");";
            }
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
        TodoDetailRAMPolicy ramPolicy = gson.fromJson(todoDetailDO.getDetailValue(), getType());
        return new WorkflowTodoDetailVO(ramPolicy, todoDetailDO);
    }


    @Override
    protected Type getType() {
        return TodoDetailRAMPolicy.class;
    }


    private WorkflowTodoDetailDO getTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO) {
        Gson gson = new GsonBuilder().create();
        TodoDetailRAMPolicy ramPolicy = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
        WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(workflowTodoDetailVO, ramPolicy.toString());
        return workflowTodoDetailDO;
    }

    @Override
    protected void createTodoDetails(long todoId, List<WorkflowTodoDetailVO> detailList) {
        String username = SessionUtils.getUsername();
        // UserDO userDO = userDao.getUserByName(username);
        List<ListPoliciesForUserResponse.Policy> list = aliyunRAMService.listPoliciesForUser(username);
        for (ListPoliciesForUserResponse.Policy policy : list) {
            WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(todoId, policy.getPolicyName(), getKey());
            WorkflowTodoDetailVO workflowTodoDetailVO = new WorkflowTodoDetailVO(new TodoDetailRAMPolicy(policy), workflowTodoDetailDO);
            detailList.add(workflowTodoDetailVO);
        }
    }

    @Override
    protected boolean invokeTodoDetails(WorkflowTodoVO todoVO) {
        List<WorkflowTodoDetailVO> todoDetails = todoVO.getTodoDetails();
        UserDO userDO = userDao.getUserById(todoVO.getApplyUserId());
        boolean result = true;

        aliyunRAMService.createUser(userDO,true);
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            TodoDetailRAMPolicy ramPolicy = (TodoDetailRAMPolicy) workflowTodoDetailVO.getDetail();
            // TODO 判断类型   已授权待解除
            if (ramPolicy.getDetailType() == TodoDetailRAMPolicy.DETAIL_TYPE_AUTHED) {
                if (!ramPolicy.isDetachPolicy()) continue;
                if (!aliyunRAMService.detachPolicyFromUser(userDO.getUsername(), ramPolicy)) {
                    result = false;
                    workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_ERR);
                } else {
                    workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_COMPLETE);
                }
                saveTodoDetail(workflowTodoDetailVO);
                continue;
            }

            if (ramPolicy.getDetailType() == TodoDetailRAMPolicy.DETAIL_TYPE_APPLY) {
                if (!ramPolicy.isAttachPolicy()) continue;
                if (!aliyunRAMService.attachPolicyToUser(userDO.getUsername(), ramPolicy)) {
                    result = false;
                    workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_ERR);
                } else {
                    workflowTodoDetailVO.setDetailStatus(WorkflowTodoDetailDO.STATUS_COMPLETE);
                }
                saveTodoDetail(workflowTodoDetailVO);
                continue;
            }
        }
        return result;
    }

}
