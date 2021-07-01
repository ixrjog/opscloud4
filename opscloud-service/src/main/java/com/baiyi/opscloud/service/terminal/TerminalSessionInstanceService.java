package com.baiyi.opscloud.service.terminal;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;

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
