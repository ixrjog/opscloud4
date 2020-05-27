package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.bo.TerminalSessionInstanceBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSessionInstance;
import com.baiyi.opscloud.xterm.model.HostSystem;

/**
 * @Author baiyi
 * @Date 2020/5/25 11:21 上午
 * @Version 1.0
 */
public class TerminalSessionInstanceBuilder {

    public static OcTerminalSessionInstance build(OcTerminalSession ocTerminalSession, HostSystem hostSystem) {
        TerminalSessionInstanceBO terminalSessionInstanceBO = TerminalSessionInstanceBO.builder()
                .sessionId(ocTerminalSession.getSessionId())
                .instanceId(hostSystem.getInstanceId())
                .systemUser(hostSystem.getSshKeyCredential().getSystemUser())
                .hostIp(hostSystem.getHost())
                .build();
        return covert(terminalSessionInstanceBO);
    }

    public static OcTerminalSessionInstance build(OcTerminalSession ocTerminalSession, HostSystem hostSystem, String duplicateInstanceId) {
        OcTerminalSessionInstance ocTerminalSessionInstance = build(ocTerminalSession, hostSystem);
        ocTerminalSessionInstance.setDuplicateInstanceId(duplicateInstanceId);
        return ocTerminalSessionInstance;
    }

    private static OcTerminalSessionInstance covert(TerminalSessionInstanceBO terminalSessionInstanceBO) {
        return BeanCopierUtils.copyProperties(terminalSessionInstanceBO, OcTerminalSessionInstance.class);
    }
}
