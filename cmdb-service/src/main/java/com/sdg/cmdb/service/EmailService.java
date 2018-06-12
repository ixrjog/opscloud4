package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.EmailItemEnum;

import com.sdg.cmdb.domain.projectManagement.ProjectManagementVO;
import com.sdg.cmdb.domain.todo.TodoDailyVO;
import com.sdg.cmdb.domain.todo.TodoDetailVO;
import com.sdg.cmdb.util.TplUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxxiao on 16/10/10.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Resource
    private ConfigCenterService configCenterService;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.EMAIL.getItemKey());
    }

    /**
     * 发送新工单通知邮件
     *
     * @param userDO
     * @param dailyVO
     * @param sponsorUser
     * @throws Exception
     */
    public boolean doSendNewTodo(UserDO userDO, TodoDailyVO dailyVO, UserDO sponsorUser) {
        logger.info("send new todo email to:" + userDO.getUsername() + " for id:" + dailyVO.getId());
        try {
            HtmlEmail email = buildHtmlEmail(userDO.getMail(), userDO.getDisplayName());

            // set the html message
            email.setHtmlMsg(TplUtils.doNewTodoNotify(sponsorUser, dailyVO, userDO.getDisplayName()));

            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 发送工单发起人更新通知邮件
     *
     * @param userDO
     * @param dailyVO
     * @return
     */
    public boolean doSendUpdateTodo(UserDO userDO, TodoDailyVO dailyVO, UserDO sponsorUser) {
        logger.info("send update todo email to:" + userDO.getUsername() + " for id:" + dailyVO.getId());
        try {
            HtmlEmail email = buildHtmlEmail(userDO.getMail(), userDO.getDisplayName());

            // set the html message
            email.setHtmlMsg(TplUtils.doNewTodoNotify(sponsorUser, dailyVO, userDO.getDisplayName()));

            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 发送工单反馈通知邮件
     *
     * @param userDO
     * @param dailyVO
     * @return
     */
    public boolean doSendAcceptFeedbackTodo(UserDO userDO, TodoDailyVO dailyVO, UserDO sponsorUser) {
        logger.info("send accept feedback todo email to:" + userDO.getUsername() + " for id:" + dailyVO.getId());
        try {
            HtmlEmail email = buildHtmlEmail(userDO.getMail(), userDO.getDisplayName());

            // set the html message
            email.setHtmlMsg(TplUtils.doAcceptFeedbackNotify(sponsorUser, dailyVO, userDO.getDisplayName()));

            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 发送工单反馈通知邮件
     *
     * @param userDO
     * @param dailyVO
     * @return
     */
    public boolean doSendInitFeedbackTodo(UserDO userDO, TodoDailyVO dailyVO, UserDO sponsorUser) {
        logger.info("send init feedback todo email to:" + userDO.getUsername() + " for id:" + dailyVO.getId());
        try {
            HtmlEmail email = buildHtmlEmail(userDO.getMail(), userDO.getDisplayName());

            // set the html message
            email.setHtmlMsg(TplUtils.doInitFeedbackNotify(sponsorUser, dailyVO, userDO.getDisplayName()));

            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 发送处理完成通知邮件
     *
     * @param userDO
     * @param dailyVO
     * @return
     */
    public boolean doFinishTodo(UserDO userDO, TodoDailyVO dailyVO, UserDO sponsorUser) {
        logger.info("send finish todo email to:" + userDO.getUsername() + " for id:" + dailyVO.getId());
        try {
            HtmlEmail email = buildHtmlEmail(userDO.getMail(), userDO.getDisplayName());

            // set the html message
            email.setHtmlMsg(TplUtils.doFinishNotify(sponsorUser, dailyVO, userDO.getDisplayName()));

            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public HtmlEmail buildHtmlEmail(String emailAddress, String displayName) throws Exception {
        HashMap<String, String> configMap = acqConifMap();
        String hostName = configMap.get(EmailItemEnum.EMAIL_HOST.getItemKey());
        String userName = configMap.get(EmailItemEnum.EMAIL_USERNAME.getItemKey());
        String userPwd = configMap.get(EmailItemEnum.EMAIL_PWD.getItemKey());

        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setAuthentication(userName, userPwd);
        email.setHostName(hostName);

        email.addTo(emailAddress, displayName);
        email.setFrom("msg@51xianqu.net", "CMDB");
        email.setSubject("工单消息~");
        email.setCharset("UTF-8");

        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");

        return email;
    }


    /**
     * 标准化工单通知
     * @param todoDetailVO
     * @return
     */
    public boolean doSendSubmitTodo(TodoDetailVO todoDetailVO) {
        List<UserDO> assigneeUsers = todoDetailVO.getAssigneeUsers();
        for (UserDO assigneeUser : assigneeUsers) {
            //System.err.println("assigneeUser:" + assigneeUser);
            logger.info("send new todo email to:" + assigneeUser.getUsername() + " for id:" + todoDetailVO.getId());
            try {
                HtmlEmail email = buildHtmlEmail(assigneeUser.getMail(), assigneeUser.getDisplayName());
                // set the html message
                email.setHtmlMsg(TplUtils.doSubmitTodoNotify(todoDetailVO, assigneeUser.getDisplayName()));
                // send the email
                email.send();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }
        return true;
    }

    public boolean doSendCompleteTodo(TodoDetailVO todoDetailVO) {
        logger.info("send complete todo email to:" + todoDetailVO.getInitiatorUsername() + " for id:" + todoDetailVO.getId());
        try {
            HtmlEmail email = buildHtmlEmail(todoDetailVO.getInitiatorUserDO().getMail(), todoDetailVO.getInitiatorUserDO().getDisplayName());
            // set the html message
            email.setHtmlMsg(TplUtils.doCompleteTodoNotify(todoDetailVO, todoDetailVO.getInitiatorUserDO().getDisplayName()));
            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 发送项目生命管理通知邮件
     *
     * @param userDO
     * @param projectManagementList
     * @return
     */
    public boolean doSendProjectHeartbeatNotify(UserDO userDO, List<ProjectManagementVO> projectManagementList) {
        logger.info("send project heartbeat notify email to:" + userDO.getUsername());
        if (projectManagementList.size() == 0) return false;
        try {
            String notifyContent = "";
            for (ProjectManagementVO pmVO : projectManagementList) {
                notifyContent += pmVO.getProjectName();
                if (!StringUtils.isEmpty(pmVO.getContent()))
                    notifyContent += "&lt;" + pmVO.getContent() + "&gt;";
                notifyContent += ";\n";
            }
            System.err.println(userDO);
            HtmlEmail email = buildHtmlEmail(userDO.getMail(), userDO.getDisplayName());

            // set the html message
            email.setHtmlMsg(TplUtils.doProjectNotify(userDO,projectManagementList.size(), notifyContent));

            // send the email
            email.send();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
