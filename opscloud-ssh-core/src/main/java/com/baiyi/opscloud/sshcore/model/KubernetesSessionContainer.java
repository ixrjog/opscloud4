package com.baiyi.opscloud.sshcore.model;

import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/19 10:43 上午
 * @Version 1.0
 */
@Data
public class KubernetesSessionContainer {

    private static Map<String, Map<String, KubernetesSession>> kubernetesSessionMap = new HashedMap<>();

    private static Map<String, Boolean> batchMap = new HashedMap<>();

    public static void setBatchFlag(String sessionId, Boolean isBatch) {
        batchMap.put(sessionId, isBatch);
    }

    public static Boolean getBatchFlagBySessionId(String sessionId) {
        return batchMap.get(sessionId);
    }

    public static void addSession(KubernetesSession kubernetesSession) {
        Map<String, KubernetesSession> sessionMap = kubernetesSessionMap.get(kubernetesSession.getSessionId());
        if (sessionMap == null) {
            sessionMap = new HashedMap<>();
            kubernetesSessionMap.put(kubernetesSession.getSessionId(), sessionMap);
        }
        sessionMap.put(kubernetesSession.getInstanceId(), kubernetesSession);
    }

    public static Map<String, KubernetesSession> getBySessionId(String sessionId) {
        return kubernetesSessionMap.get(sessionId);
    }

    public static KubernetesSession getBySessionId(String sessionId, String instanceId) {
        Map<String, KubernetesSession> sessionMap = kubernetesSessionMap.get(sessionId);
        if (sessionMap == null) {
            return null;
        } else {
            return sessionMap.get(instanceId);
        }
    }

    public static void removeSession(String sessionId, String instanceId) {
        Map<String, KubernetesSession> sessionMap = kubernetesSessionMap.get(sessionId);
        if (sessionMap != null) {
            sessionMap.remove(instanceId);
        }
    }

    /**
     * 关闭并移除会话
     *
     * @param sessionId
     * @param instanceId
     */
    public static void closeSession(String sessionId, String instanceId) {
        KubernetesSession kubernetesSession = KubernetesSessionContainer.getBySessionId(sessionId, instanceId);
        if (kubernetesSession != null) {
            kubernetesSession.getWatchKubernetesTerminalOutputTask().close();
            if (kubernetesSession.getLogWatch() != null) {
                kubernetesSession.getLogWatch().close();
            }
            if (kubernetesSession.getExecWatch() != null) {
                kubernetesSession.getExecWatch().close();
            }
            kubernetesSession.setInputToChannel(null);
            kubernetesSession.setSessionOutput(null);
            kubernetesSession.setInstanceId(null);
        }
        removeSession(sessionId, instanceId);
        kubernetesSession = null;
    }

}