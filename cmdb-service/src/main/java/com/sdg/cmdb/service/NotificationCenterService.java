package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;

public interface NotificationCenterService {

    /**
     * 发送工作流邮件通知
     * @param workflowTodoVO
     * @param userDO
     * @return
     */
    boolean notifWorkflowTodo(WorkflowTodoVO workflowTodoVO, UserDO userDO);


}
