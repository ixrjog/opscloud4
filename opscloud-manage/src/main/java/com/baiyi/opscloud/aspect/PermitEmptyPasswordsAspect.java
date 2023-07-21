package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.PermitEmptyPasswords;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.facade.auth.UserTokenFacade;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/4 3:44 下午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PermitEmptyPasswordsAspect {

    private final UserService userService;

    private final UserTokenFacade userTokenFacade;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.domain.annotation.PermitEmptyPasswords)")
    public void annotationPoint() {
    }

    @Around("@annotation(permitEmptyPasswords)")
    public Object around(ProceedingJoinPoint joinPoint, PermitEmptyPasswords permitEmptyPasswords) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名称
        String[] params = methodSignature.getParameterNames();
        // 获取参数值
        Object[] args = joinPoint.getArgs();
        if (params != null && params.length != 0) {
            Object obj = args[0];
            if (obj instanceof LoginParam.Login loginParam) {
                User user = userService.getByUsername(loginParam.getUsername());
                // 判断用户是否有效
                if (user == null || !user.getIsActive()) {
                    throw new AuthenticationException(ErrorEnum.AUTH_USER_LOGIN_FAILURE);
                }
                if (loginParam.isEmptyPassword()) {
                    if (StringUtils.isEmpty(user.getPassword())) {
                        // 空密码登录成功
                        return userTokenFacade.userLogin(user);
                    }
                    throw new AuthenticationException(ErrorEnum.AUTH_USER_LOGIN_FAILURE);
                }
            }
        }
        return joinPoint.proceed();
    }

}
