package com.baiyi.caesar.sshserver.aspect;

import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.service.auth.AuthRoleService;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.sshserver.PromptColor;
import com.baiyi.caesar.sshserver.SshShellHelper;
import com.baiyi.caesar.sshserver.annotation.InvokeSessionUser;
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

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/28 2:54 下午
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class InvokeSessionUserAspect {

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

    @Pointcut(value = "@annotation(com.baiyi.caesar.sshserver.annotation.InvokeSessionUser)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(invokeSessionUser)")
    public Object around(ProceedingJoinPoint joinPoint, InvokeSessionUser invokeSessionUser) throws Throwable {
        User user = userService.getByUsername(helper.getSshSession().getUsername());
        if (user == null || !user.getIsActive()) {
            helper.print("未经授权的访问！", PromptColor.RED);
            throw new ExitRequest();
        }
        com.baiyi.caesar.common.util.SessionUtil.setUserId(user.getId());
        com.baiyi.caesar.common.util.SessionUtil.setUsername(user.getUsername());
        if (invokeSessionUser.invokeAdmin())
            com.baiyi.caesar.common.util.SessionUtil.setIsAdmin(isAdmin(user.getUsername()));
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
