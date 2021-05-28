package com.baiyi.caesar.common.redis;

import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/5/30 1:03 下午
 * @Version 1.0
 */
public class TerminalKeyUtil {

    public static String buildAuditLogKey(String sessionId, String instanceId) {
        return Joiner.on("#").join(sessionId, instanceId, "auditLog");
    }

    public static String buildCommanderLogKey(String sessionId, String instanceId) {
        return Joiner.on("#").join(sessionId, instanceId, "commander");
    }


    public static String buildSessionHeartbeatKey(String sessionId) {
        return Joiner.on("#").join(sessionId, "heartbeat");
    }

}
