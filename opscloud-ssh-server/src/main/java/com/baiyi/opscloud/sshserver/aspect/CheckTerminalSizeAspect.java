package com.baiyi.opscloud.sshserver.aspect;

import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.annotation.CheckTerminalSize;
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
public class CheckTerminalSizeAspect {

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

    @Pointcut(value = "@annotation(com.baiyi.opscloud.sshserver.annotation.CheckTerminalSize)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(checkTerminalSize)")
    public Object around(ProceedingJoinPoint joinPoint, CheckTerminalSize checkTerminalSize) throws Throwable {

        String message = null;
        Size size = terminal.getSize();
        if (checkTerminalSize.cols() != 0) {
            if (checkTerminalSize.cols() > size.getColumns())
                message = "列不能小于 " + checkTerminalSize.cols();
        }

        if (checkTerminalSize.rows() != 0) {
            if (checkTerminalSize.rows() > size.getRows())
                message = Joiner.on(",").skipNulls().join(message, "行不能小于 " + checkTerminalSize.rows());
        }
        if (!StringUtils.isEmpty(message)) {
            helper.print("请调整您的终端让其符合最佳显示标准: " + message, PromptColor.RED);
            return joinPoint;
        }
        joinPoint.proceed();
        return joinPoint;
    }

}
