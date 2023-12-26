package com.baiyi.opscloud.terminal.audit.impl;

import com.baiyi.opscloud.sshcore.message.audit.BaseAuditMessage;
import com.baiyi.opscloud.terminal.audit.ITerminalAuditHandler;
import com.baiyi.opscloud.terminal.audit.TerminalAuditHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:58 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractTerminalAuditHandler<T extends BaseAuditMessage> implements ITerminalAuditHandler, InitializingBean {

    abstract protected T getMessage(String message);

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        TerminalAuditHandlerFactory.register(this);
    }

}