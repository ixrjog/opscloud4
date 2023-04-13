package com.baiyi.opscloud.sshcore.util;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.sshcore.model.SessionOutput;
import com.baiyi.opscloud.sshcore.model.UserSessionsOutput;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:48 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class SessionOutputUtil {

    private static RedisUtil redisUtil;

    @Autowired
    private void setRedisUtil(RedisUtil redisUtil) {
        SessionOutputUtil.redisUtil = redisUtil;
    }

    private static final Map<String, UserSessionsOutput> USER_SESSIONS_OUTPUT_MAP = new ConcurrentHashMap<>();

    private SessionOutputUtil() {
    }

    /**
     * removes session for user session
     *
     * @param sessionId session id
     */
    public static void removeUserSession(String sessionId) {
        UserSessionsOutput userSessionsOutput = USER_SESSIONS_OUTPUT_MAP.get(sessionId);
        if (userSessionsOutput != null) {
            userSessionsOutput.getSessionOutputMap().clear();
        }
        USER_SESSIONS_OUTPUT_MAP.remove(sessionId);
    }

    /**
     * removes session output for host system
     *
     * @param sessionId  session id
     * @param instanceId id of host system instance
     */
    public static void removeOutput(String sessionId, String instanceId) {
        UserSessionsOutput userSessionsOutput = USER_SESSIONS_OUTPUT_MAP.get(sessionId);
        if (userSessionsOutput != null) {
            userSessionsOutput.getSessionOutputMap().remove(instanceId);
        }
    }

    /**
     * adds a new output
     *
     * @param sessionOutput session output object
     */
    public static void addOutput(SessionOutput sessionOutput) {
        UserSessionsOutput userSessionsOutput = USER_SESSIONS_OUTPUT_MAP.get(sessionOutput.getSessionId());
        if (userSessionsOutput == null) {
            USER_SESSIONS_OUTPUT_MAP.put(sessionOutput.getSessionId(), new UserSessionsOutput());
            userSessionsOutput = USER_SESSIONS_OUTPUT_MAP.get(sessionOutput.getSessionId());
        }
        userSessionsOutput.getSessionOutputMap().put(sessionOutput.getInstanceId(), sessionOutput);
    }

    /**
     * adds a new output
     *
     * @param sessionId  session id
     * @param instanceId id of host system instance
     * @param value      Array that is the source of characters
     * @param offset     The initial offset
     * @param count      The length
     */
    public static void addToOutput(String sessionId, String instanceId, char[] value, int offset, int count) {
        UserSessionsOutput userSessionsOutput = USER_SESSIONS_OUTPUT_MAP.get(sessionId);
        if (userSessionsOutput != null) {
            Map<String, SessionOutput> sessionOutputMap = userSessionsOutput.getSessionOutputMap();
            if (sessionOutputMap.containsKey(instanceId)) {
                sessionOutputMap.get(instanceId).getOutput().append(value, offset, count);
            }
        }
    }

    public static SessionOutput getSessionOutput(String sessionId, String instanceId) {
        UserSessionsOutput userSessionsOutput = USER_SESSIONS_OUTPUT_MAP.get(sessionId);
        if (userSessionsOutput != null) {
            return userSessionsOutput.getSessionOutputMap().get(instanceId);
        }
        return null;
    }

    /**
     * returns list of output lines
     *
     * @param sessionId session id object
     * @return session output list
     */
    public static List<SessionOutput> getOutput(String sessionId) {
        List<SessionOutput> outputList = Lists.newArrayList();
        UserSessionsOutput userSessionsOutput = USER_SESSIONS_OUTPUT_MAP.get(sessionId);
        if (userSessionsOutput != null) {
            userSessionsOutput.getSessionOutputMap().keySet().forEach(instanceId -> {
                //get output chars and set to output
                try {
                    SessionOutput sessionOutput = userSessionsOutput.getSessionOutputMap().get(instanceId);
                    if (sessionOutput != null && sessionOutput.getOutput() != null
                            && StringUtils.isNotEmpty(sessionOutput.getOutput())) {
                        outputList.add(sessionOutput);
                        userSessionsOutput.getSessionOutputMap().put(instanceId, new SessionOutput(sessionId, sessionOutput));
                    }
                } catch (Exception ex) {
                    log.error(ex.toString(), ex);
                }
            });
        }
        return outputList;
    }

    /**
     * 审计日志
     *
     * @param sessionId
     * @param instanceId
     * @param sessionOutput
     */
    @Deprecated
    private static void record(String sessionId, String instanceId, SessionOutput sessionOutput) {
        String outputStr = sessionOutput.getOutput().toString();
        String auditLog;

        // 输出太多截断
        if (sessionOutput.getOutput().length() > 1024) {
            auditLog = sessionOutput.getOutput().substring(0, subOutputLine(outputStr)) + "\r\n";
        } else {
            auditLog = sessionOutput.getOutput().toString();
        }
        String cacheKey = Joiner.on("_").join(sessionId, instanceId, "auditLog");
        String logRepo;
        if (redisUtil.hasKey(cacheKey)) {
            logRepo = redisUtil.get(cacheKey) + auditLog;
        } else {
            logRepo = auditLog;
        }
        redisUtil.set(cacheKey, logRepo, 6000);
//        if (logRepo.length() > 10240)
//            AuditRecordHandler.recordAuditLog(sessionId, instanceId);
    }

    private static int subOutputLine(String auditContent) {
        int maxLine = 5;
        int index = 0;
        int line = 1;
        while (line <= maxLine) {
            index = auditContent.indexOf("\r\n", index) + 2;
            line++;
        }
        return index;
    }

}