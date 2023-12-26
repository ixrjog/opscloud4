package com.baiyi.opscloud.sshserver.aop.aspect;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.aop.annotation.TerminalSize;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/7/9 10:17 上午
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class TerminalSizeAspect {

    private SshShellHelper helper;

    @Autowired
    @Lazy
    public void setSshShellHelper(SshShellHelper helper) {
        this.helper = helper;
    }

    private Terminal terminal;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @Pointcut(value = "@annotation(com.baiyi.opscloud.sshserver.aop.annotation.TerminalSize)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(terminalSize)")
    public Object around(ProceedingJoinPoint joinPoint, TerminalSize terminalSize) throws Throwable {
        String message = null;
        Size size = terminal.getSize();
        if (terminalSize.cols() != 0) {
            if (terminalSize.cols() > size.getColumns()) {
                message = StringFormatter.format("列不能小于 {}", terminalSize.cols());
            }
        }
        if (terminalSize.rows() != 0) {
            if (terminalSize.rows() > size.getRows()) {
                message = Joiner.on(",").skipNulls().join(message, StringFormatter.format("行不能小于 {}", terminalSize.rows()));
            }
        }
        if (!StringUtils.isEmpty(message)) {
            helper.print(StringFormatter.format("请调整终端让其符合最佳显示尺寸: {}", message), PromptColor.RED);
            return joinPoint;
        }
        joinPoint.proceed();
        return joinPoint;
    }

}