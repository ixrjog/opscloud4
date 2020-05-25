package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:26 下午
 * @Version 1.0
 */
public interface TerminalFacade {

    void addOcTerminalSession(OcTerminalSession ocTerminalSession);

    void updateOcTerminalSession(OcTerminalSession ocTerminalSession);
}
