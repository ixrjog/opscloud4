/**
 * Copyright (C) 2013 Loophole, LLC
 * <p>
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the GNU Affero General Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.baiyi.opscloud.xterm.util;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.bae64.CacheKeyUtils;
import com.baiyi.opscloud.xterm.handler.AuditLogHandler;
import com.baiyi.opscloud.xterm.model.SessionOutput;
import com.baiyi.opscloud.xterm.model.UserSessionsOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility to is used to store the output for a session until the ajax call that brings it to the screen
 */
@Slf4j
@Component
public class SessionOutputUtil {

    private static RedisUtil redisUtil;

    @Autowired
    private void setRedisUtil(RedisUtil redisUtil) {
        SessionOutputUtil.redisUtil = redisUtil;
    }

    private static Map<String, UserSessionsOutput> userSessionsOutputMap = new ConcurrentHashMap<>();

    private SessionOutputUtil() {
    }

    /**
     * removes session for user session
     *
     * @param sessionId session id
     */
    public static void removeUserSession(String sessionId) {
        UserSessionsOutput userSessionsOutput = userSessionsOutputMap.get(sessionId);
        if (userSessionsOutput != null) {
            userSessionsOutput.getSessionOutputMap().clear();
        }
        userSessionsOutputMap.remove(sessionId);

    }

    /**
     * removes session output for host system
     *
     * @param sessionId  session id
     * @param instanceId id of host system instance
     */
    public static void removeOutput(String sessionId, String instanceId) {

        UserSessionsOutput userSessionsOutput = userSessionsOutputMap.get(sessionId);
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

        UserSessionsOutput userSessionsOutput = userSessionsOutputMap.get(sessionOutput.getSessionId());
        if (userSessionsOutput == null) {
            userSessionsOutputMap.put(sessionOutput.getSessionId(), new UserSessionsOutput());
            userSessionsOutput = userSessionsOutputMap.get(sessionOutput.getSessionId());
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
    public static void addToOutput(String sessionId, String instanceId, char value[], int offset, int count) {

        UserSessionsOutput userSessionsOutput = userSessionsOutputMap.get(sessionId);
        if (userSessionsOutput != null) {
            userSessionsOutput.getSessionOutputMap().get(instanceId).getOutput().append(value, offset, count);
        }

    }


    /**
     * returns list of output lines
     *
     * @param sessionId session id object
     * @return session output list
     */
    public static List<SessionOutput> getOutput(String sessionId) {
        List<SessionOutput> outputList = new ArrayList<>();
        UserSessionsOutput userSessionsOutput = userSessionsOutputMap.get(sessionId);
        if (userSessionsOutput != null) {
            userSessionsOutput.getSessionOutputMap().keySet().forEach(instanceId->{
                //get output chars and set to output
                try {
                    SessionOutput sessionOutput = userSessionsOutput.getSessionOutputMap().get(instanceId);
                    if (sessionOutput != null && sessionOutput.getOutput() != null
                            && StringUtils.isNotEmpty(sessionOutput.getOutput())) {

                        outputList.add(sessionOutput);
                        userSessionsOutput.getSessionOutputMap().put(instanceId, new SessionOutput(sessionId, sessionOutput));
                        auditing(sessionId, instanceId, sessionOutput);
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
    private static void auditing(String sessionId, String instanceId, SessionOutput sessionOutput) {
        String outputStr = sessionOutput.getOutput().toString();
        String auditLog;

        if (sessionOutput.getOutput().length() > 1024) { // 输出太多截断
            auditLog = sessionOutput.getOutput().substring(0, subOutputLine(outputStr)) + "\r\n";
        } else {
            auditLog = sessionOutput.getOutput().toString();
        }

        String cacheKey = CacheKeyUtils.getTermAuditLogKey(sessionId, instanceId);
        String logRepo;
        if (redisUtil.hasKey(cacheKey)) {
            logRepo = new StringBuilder((String) redisUtil.get(cacheKey)).append(auditLog).toString();
        } else {
            logRepo = auditLog;
        }
        redisUtil.set(cacheKey, logRepo, 6000 );

        if (logRepo.length() > 10240)
            AuditLogHandler.writeAuditLog(sessionId, instanceId);

    }

    private static int subOutputLine(String auditContent) {
        int maxLine = 5;
        int index = 0;
        int line = 1;
        while (true) {
            if (line > maxLine) break;
            int find = auditContent.indexOf("\r\n", index) + 2;
            if (find != 0) {
                index = find;
            } else {
                break;
            }
            line++;
        }
        return index;
    }


}
