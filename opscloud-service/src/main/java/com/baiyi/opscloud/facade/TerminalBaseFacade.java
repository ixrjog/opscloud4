package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSessionInstance;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:26 下午
 * @Version 1.0
 */
public interface TerminalBaseFacade {

    void addOcTerminalSession(OcTerminalSession ocTerminalSession);

    void updateOcTerminalSession(OcTerminalSession ocTerminalSession);

    void addOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance);

    void updateOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance);

    OcTerminalSessionInstance queryOcTerminalSessionInstanceByUniqueKey(String sessionId, String instanceId);
}
