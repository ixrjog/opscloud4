package com.baiyi.opscloud.common.util.bae64;

import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/5/30 1:03 下午
 * @Version 1.0
 */
public class CacheKeyUtils {

    public static String getTermAuditLogKey(String sessionId, String instanceId) {
        return Joiner.on("#").join(sessionId, instanceId, "auditLog");
    }

    public static String getTermCommanderLogKey(String sessionId, String instanceId) {
        return Joiner.on("#").join(sessionId, instanceId, "commander");
    }


    public static String getTermSessionHeartbeatKey(String sessionId) {
        return Joiner.on("#").join(sessionId, "heartbeat");
    }

}
