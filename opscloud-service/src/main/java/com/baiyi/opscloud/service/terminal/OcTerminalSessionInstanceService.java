package com.baiyi.opscloud.service.terminal;

import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSessionInstance;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/25 11:12 上午
 * @Version 1.0
 */
public interface OcTerminalSessionInstanceService {

    void addOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance);

    void updateOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance);

    OcTerminalSessionInstance queryOcTerminalSessionInstanceByUniqueKey(String sessionId, String instanceId);

    OcTerminalSessionInstance queryOcTerminalSessionInstanceById(int id);

    List<OcTerminalSessionInstance> queryOcTerminalSessionInstanceBySessionId(String sessionId);
}
