package com.baiyi.opscloud.sshserver.aop.aspect;

import com.baiyi.opscloud.sshserver.aop.annotation.ScreenClear;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/7/2 6:23 下午
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class ScreenClearAspect {

    private Terminal terminal;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @Pointcut(value = "@annotation(com.baiyi.opscloud.sshserver.aop.annotation.ScreenClear)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(screenClear)")
    public Object around(ProceedingJoinPoint joinPoint, ScreenClear screenClear) throws Throwable {
        this.terminal.puts(InfoCmp.Capability.clear_screen, new Object[0]);
        joinPoint.proceed();
        return joinPoint;
    }

}