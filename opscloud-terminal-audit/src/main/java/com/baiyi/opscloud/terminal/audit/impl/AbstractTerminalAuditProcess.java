package com.baiyi.opscloud.terminal.audit.impl;

import com.baiyi.opscloud.sshcore.message.audit.BaseAuditMessage;
import com.baiyi.opscloud.terminal.audit.ITerminalAuditProcess;
import com.baiyi.opscloud.terminal.audit.TerminalAuditProcessFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:58 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractTerminalAuditProcess<T extends BaseAuditMessage> implements ITerminalAuditProcess, InitializingBean {

    abstract protected T getMessage(String message);

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        TerminalAuditProcessFactory.register(this);
    }
}
