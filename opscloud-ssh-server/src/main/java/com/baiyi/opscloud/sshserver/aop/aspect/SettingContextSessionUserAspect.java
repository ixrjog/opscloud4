package com.baiyi.opscloud.sshserver.aop.aspect;

import com.baiyi.opscloud.common.base.AccessLevel;
import com.baiyi.opscloud.common.constants.enums.SessionSource;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.aop.annotation.SettingContextSessionUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Component;



/**
 * @Author baiyi
 * @Date 2021/6/28 2:54 下午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component

public class SettingContextSessionUserAspect {

    @Resource
    private UserService userService;

    private SshShellHelper helper;

    @Autowired
    @Lazy
    public void setSshShellHelper(SshShellHelper helper) {
        this.helper = helper;
    }

    @Resource
    private AuthRoleService authRoleService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.sshserver.aop.annotation.SettingContextSessionUser)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(invokeSessionUser)")
    public Object around(ProceedingJoinPoint joinPoint, SettingContextSessionUser invokeSessionUser) throws Throwable {
        if (invokeSessionUser.source() == SessionSource.SSH_SHELL) {
            User user = userService.getByUsername(helper.getSshSession().getUsername());
            if (user == null || !user.getIsActive()) {
                helper.print("未经授权的访问！", PromptColor.RED);
                throw new ExitRequest();
            }
            SessionHolder.setUserId(user.getId());
            SessionHolder.setUsername(user.getUsername());
            if (invokeSessionUser.invokeAdmin()) {
                SessionHolder.setIsAdmin(isAdmin(user.getUsername()));
            }
        }
        if (invokeSessionUser.source() == SessionSource.TOKEN) {
            if (invokeSessionUser.invokeAdmin()) {
                User user = userService.getByUsername(SessionHolder.getUsername());
                SessionHolder.setUserId(user.getId());
                SessionHolder.setIsAdmin(isAdmin(SessionHolder.getUsername()));
            }
        }
        joinPoint.proceed();
        return joinPoint;
    }

    /**
     * OPS角色以上即认定为系统管理员
     *
     * @return
     */
    private boolean isAdmin(String username) {
        int accessLevel = authRoleService.getRoleAccessLevelByUsername(username);
        return accessLevel >= AccessLevel.OPS.getLevel();
    }

}