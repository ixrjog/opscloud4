package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoDO;
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO;
import com.sdg.cmdb.service.NotificationCenterService;

import com.sdg.cmdb.util.TplWorkflowUtils;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class NotificationCenterServiceImpl implements NotificationCenterService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationCenterServiceImpl.class);

    @Value("#{cmdb['email.host']}")
    private String emailHost;

    @Value("#{cmdb['email.user']}")
    private String emailUser;

    @Value("#{cmdb['email.passwd']}")
    private String emailPasswd;

    @Value("#{cmdb['external.url']}")
    private String externalUrl;


    @Override
    public boolean notifWorkflowTodo(WorkflowTodoVO workflowTodoVO, UserDO userDO) {
        //System.err.println("assigneeUser:" + assigneeUser);
        logger.info("send new todo email to:" + userDO.getUsername() + " for id:" + workflowTodoVO.getId());
        try {
            HtmlEmail email = buildHtmlEmail(userDO.getMail(), userDO.getDisplayName());
            // set the html message
            email.setHtmlMsg(TplWorkflowUtils.doWorkflowTodoNotify(workflowTodoVO, userDO, externalUrl, getTodoPhaseDesc(workflowTodoVO)));
            email.send();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private String getTodoPhaseDesc(WorkflowTodoVO workflowTodoVO) {
        int todoPhase = workflowTodoVO.getTodoPhase();
        if (todoPhase == WorkflowTodoDO.TODO_PHASE_QA_APPROVAL)
            return "待审批";
        if (todoPhase == WorkflowTodoDO.TODO_PHASE_TL_APPROVAL)
            return "待审批";
        if (todoPhase == WorkflowTodoDO.TODO_PHASE_DL_APPROVAL)
            return "待审批";
        if (todoPhase == WorkflowTodoDO.TODO_PHASE_AUDITING)
            return "待审核";
        if (todoPhase == WorkflowTodoDO.TODO_PHASE_COMPLETE) {
            if (workflowTodoVO.getTodoStatus() == WorkflowTodoDO.TODO_STATUS_COMPLETE) {
                return "已完成";
            } else {
                return "被拒绝或错误";
            }
        }

        return "状态未定义";
    }

    private HtmlEmail buildHtmlEmail(String emailAddress, String displayName) throws Exception {
        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setAuthentication(emailUser, emailPasswd);
        email.setHostName(emailHost);
        email.addTo(emailAddress, displayName);
        email.setFrom("ops@gegejia.com", "opscloud");
        email.setSubject("工作流通知~");
        email.setCharset("UTF-8");
        email.setSslSmtpPort("465");
        email.setSSLOnConnect(true);
        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");
        return email;
    }
}
