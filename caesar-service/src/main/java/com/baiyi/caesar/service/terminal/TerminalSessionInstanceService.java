package com.baiyi.caesar.service.terminal;

import com.baiyi.caesar.domain.generator.caesar.TerminalSessionInstance;

/**
 * @Author baiyi
 * @Date 2021/5/28 11:28 上午
 * @Version 1.0
 */
public interface TerminalSessionInstanceService {

    void add(TerminalSessionInstance terminalSessionInstance);

    void update(TerminalSessionInstance terminalSessionInstance);

    TerminalSessionInstance getByUniqueKey(String sessionId,String instanceId);
}
