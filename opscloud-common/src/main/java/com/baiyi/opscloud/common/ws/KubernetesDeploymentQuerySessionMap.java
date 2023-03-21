package com.baiyi.opscloud.common.ws;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/3/20 16:22
 * @Version 1.0
 */
public class KubernetesDeploymentQuerySessionMap {

    /**
     * Map<String sessionId, String message>
     */
    private static final Map<String, String> SESSION_QUERY_MAP = new HashedMap<>();

    public static void addSessionQueryMap(String sessionId, String message) {
        KubernetesDeploymentQuerySessionMap.SESSION_QUERY_MAP.put(sessionId, message);
    }

    public static boolean sessionQueryMapContainsKey(String sessionId) {
        return KubernetesDeploymentQuerySessionMap.SESSION_QUERY_MAP.containsKey(sessionId);
    }

    public static String getSessionQueryMap(String sessionId) {
        return KubernetesDeploymentQuerySessionMap.SESSION_QUERY_MAP.get(sessionId);
    }

    public static void removeSessionQueryMap(String sessionId) {
        KubernetesDeploymentQuerySessionMap.SESSION_QUERY_MAP.remove(sessionId);
    }

}
