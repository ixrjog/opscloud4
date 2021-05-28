package com.baiyi.caesar.service.terminal;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:38 上午
 * @Version 1.0
 */
public interface TerminalSessionService {

    void add(TerminalSession terminalSession);

    void update(TerminalSession terminalSession);
}
