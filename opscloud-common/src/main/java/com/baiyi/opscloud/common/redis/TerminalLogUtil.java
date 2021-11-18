package com.baiyi.opscloud.common.redis;

import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/5/30 1:03 下午
 * @Version 1.0
 */
public class TerminalLogUtil {

    public static String toAuditLogKey(String sessionId, String instanceId) {
        return Joiner.on("_").join(sessionId, instanceId, "auditLog");
    }

}
