package com.baiyi.opscloud.common.leo.session;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/11/28 09:53
 * @Version 1.0
 */
@Slf4j
@Data
public class LeoBuildQuerySessionMap {

    /**
     * Map<String sessionId, Map<String messageType, String message>>
     */
    private static Map<String, Map<String, String>> sessionQueryMap = new HashedMap<>();

    public static void addSessionQueryMap(String sessionId, Map<String, String> queryMap) {
        LeoBuildQuerySessionMap.sessionQueryMap.put(sessionId, queryMap);
    }

    public static void addSessionQueryMap(String sessionId, String messageType, String message) {
        Map<String, String> queryMap;
        if (LeoBuildQuerySessionMap.sessionQueryMap.containsKey(sessionId)) {
            queryMap = LeoBuildQuerySessionMap.sessionQueryMap.get(sessionId);
        } else {
            queryMap = Maps.newHashMap();
        }
        queryMap.put(messageType, message);
        LeoBuildQuerySessionMap.sessionQueryMap.put(sessionId, queryMap);
    }

    public static boolean sessionQueryMapContainsKey(String sessionId) {
        return LeoBuildQuerySessionMap.sessionQueryMap.containsKey(sessionId);
    }

    public static Map<String, String> getSessionQueryMap(String sessionId) {
        return LeoBuildQuerySessionMap.sessionQueryMap.get(sessionId);
    }

    public static void removeSessionQueryMap(String sessionId) {
        LeoBuildQuerySessionMap.sessionQueryMap.remove(sessionId);
    }

}