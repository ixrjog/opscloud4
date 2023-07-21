package com.baiyi.opscloud.common.leo.session;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/6 18:12
 * @Version 1.0
 */
@Slf4j
public class LeoDeployQuerySessionMap {

    /**
     * Map<String sessionId, Map<String messageType, String message>>
     */
    private static final Map<String, Map<String, String>> sessionQueryMap = new HashedMap<>();

    public static void addSessionQueryMap(String sessionId, Map<String, String> queryMap) {
        LeoDeployQuerySessionMap.sessionQueryMap.put(sessionId, queryMap);
    }

    public static void addSessionQueryMap(String sessionId, String messageType, String message) {
        Map<String, String> queryMap;
        if (LeoDeployQuerySessionMap.sessionQueryMap.containsKey(sessionId)) {
            queryMap = LeoDeployQuerySessionMap.sessionQueryMap.get(sessionId);
        } else {
            queryMap = Maps.newHashMap();
        }
        queryMap.put(messageType, message);
        LeoDeployQuerySessionMap.sessionQueryMap.put(sessionId, queryMap);
    }

    public static void removeSessionQueryMap(String sessionId, String messageType) {
        Map<String, String> queryMap;
        if (LeoDeployQuerySessionMap.sessionQueryMap.containsKey(sessionId)) {
            queryMap = LeoDeployQuerySessionMap.sessionQueryMap.get(sessionId);
            queryMap.remove(messageType);
        }
    }

    public static boolean sessionQueryMapContainsKey(String sessionId) {
        return LeoDeployQuerySessionMap.sessionQueryMap.containsKey(sessionId);
    }

    public static Map<String, String> getSessionQueryMap(String sessionId) {
        return LeoDeployQuerySessionMap.sessionQueryMap.get(sessionId);
    }

    public static void removeSessionQueryMap(String sessionId) {
        LeoDeployQuerySessionMap.sessionQueryMap.remove(sessionId);
    }

}