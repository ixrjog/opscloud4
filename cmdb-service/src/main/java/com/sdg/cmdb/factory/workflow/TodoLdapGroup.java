package com.sdg.cmdb.factory.workflow;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.domain.workflow.detail.*;
import com.sdg.cmdb.service.LdapService;
import com.sdg.cmdb.util.SessionUtils;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class TodoLdapGroup extends TodoAbs implements Serializable {
    private static final long serialVersionUID = 5883679818912800761L;

    private final String TODO_KEY = "LDAPGROUP";

    @Resource
    private LdapService ldapService;

    @Override
    protected Type getType() {
        return TodoDetailLdapGroup.class;
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
        String notice = "外部平台权限组(Ldap)：";
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            WorkflowTodoDetailDO workflowTodoDetailDO = getTodoDetailDO(workflowTodoDetailVO);
            if (!saveTodoDetail(workflowTodoDetailDO)) return workflowTodoVO;
            // TODO 插入工作流详情
            Gson gson = new GsonBuilder().create();
            TodoDetailLdapGroup todoDetail = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
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
        TodoDetailLdapGroup ldapGroupDetail = gson.fromJson(todoDetailDO.getDetailValue(), getType());
        return new WorkflowTodoDetailVO(ldapGroupDetail, todoDetailDO);
    }


    private WorkflowTodoDetailDO getTodoDetailDO(WorkflowTodoDetailVO workflowTodoDetailVO) {
        Gson gson = new GsonBuilder().create();
        TodoDetailLdapGroup ldapGroupDetail = gson.fromJson(JSON.toJSONString(workflowTodoDetailVO.getDetail()), getType());
        WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(workflowTodoDetailVO, ldapGroupDetail.toString());
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
        List<TodoDetailLdapGroup> ldapGroups = ldapService.getUserWorkflowLdapGroup(SessionUtils.getUsername());
        for (TodoDetailLdapGroup ldapGroup : ldapGroups) {
            WorkflowTodoDetailDO workflowTodoDetailDO = new WorkflowTodoDetailDO(todoId, ldapGroup.getLdapGroupDO().getCn(), getKey());
            //workflowTodoDetailDO.setContent(ldapGroup.getContent());
            WorkflowTodoDetailVO workflowTodoDetailVO = new WorkflowTodoDetailVO(ldapGroup, workflowTodoDetailDO);
            detailList.add(workflowTodoDetailVO);
        }
    }

    @Override
    protected boolean invokeTodoDetails(WorkflowTodoVO todoVO) {
        List<WorkflowTodoDetailVO> todoDetails = todoVO.getTodoDetails();
        UserDO userDO = userDao.getUserById(todoVO.getApplyUserId());
        boolean result = true;
        for (WorkflowTodoDetailVO workflowTodoDetailVO : todoDetails) {
            TodoDetailLdapGroup ldapGroup = (TodoDetailLdapGroup) workflowTodoDetailVO.getDetail();
            // TODO 判断是否申请
            if (!ldapGroup.isApply())
                continue;
            if (ldapService.addMemberToGroup(userDO, ldapGroup.getLdapGroupDO().getCn())) {
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
