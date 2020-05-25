package com.baiyi.opscloud.service.terminal;

import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:18 下午
 * @Version 1.0
 */
public interface OcTerminalSessionService {

    OcTerminalSession queryOcTerminalSessionBySessionId(String sessionId);

    void addOcTerminalSession(OcTerminalSession ocTerminalSession);

    void updateOcTerminalSession(OcTerminalSession ocTerminalSession);
}
