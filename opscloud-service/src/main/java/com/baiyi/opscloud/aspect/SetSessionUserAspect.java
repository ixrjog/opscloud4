package com.baiyi.opscloud.aspect;

import com.baiyi.opscloud.common.annotation.SetSessionUser;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/3/7 11:19 AM
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(1)
public class SetSessionUserAspect {

    private final UserService userService;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.common.annotation.SetSessionUser)")
    public void annotationPoint() {
    }

    @Before("@annotation(setSessionUser)")
    public void doBefore(JoinPoint joinPoint, SetSessionUser setSessionUser) throws Throwable {
        User user = userService.getById(setSessionUser.userId());
        if (user == null) {
            log.error("设置当前会话用户: 用户不存在 userId = {}", setSessionUser.userId());
        }
        if (!setSessionUser.force()) {
            if (!StringUtils.isEmpty(SessionUtil.getUsername())) {
                log.info("设置当前会话用户: 当前用户已存在 userId = {}", setSessionUser.userId());
            }
        }
        SessionUtil.setUsername(user.getUsername());
        SessionUtil.setUserId(user.getId());
    }

}
