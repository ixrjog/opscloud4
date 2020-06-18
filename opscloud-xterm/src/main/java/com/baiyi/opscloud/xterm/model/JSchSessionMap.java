package com.baiyi.opscloud.xterm.model;

import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

@Data
public class JSchSessionMap {

    private static Map<String, Map<String, JSchSession>> jSchSessionMap = new HashedMap<>();

    private static Map<String, Boolean> batchMap = new HashedMap<>();

    public static void setBatch(String sessionId, Boolean isBatch) {
        batchMap.put(sessionId, isBatch);
    }

    public static Boolean getBatchBySessionId(String sessionId) {
        return batchMap.get(sessionId);
    }

    public static void addSession(JSchSession jSchSession) {
        Map<String, JSchSession> sessionMap = jSchSessionMap.get(jSchSession.getSessionId());
        if (sessionMap == null) {
            sessionMap = new HashedMap<>();
            jSchSessionMap.put(jSchSession.getSessionId(), sessionMap);
        }
        sessionMap.put(jSchSession.getInstanceId(), jSchSession);

    }

    public static Map<String, JSchSession> getBySessionId(String sessionId) {
        return jSchSessionMap.get(sessionId);
    }

    public static JSchSession getBySessionId(String sessionId, String instanceId) {
        Map<String, JSchSession> sessionMap = jSchSessionMap.get(sessionId);
        if (sessionMap == null) {
            return null;
        } else {
            return sessionMap.get(instanceId);
        }
    }

    public static void removeSession(String sessionId, String instanceId) {
        Map<String, JSchSession> sessionMap = jSchSessionMap.get(sessionId);
        if (sessionMap != null)
            sessionMap.remove(instanceId);
    }
}
