package com.baiyi.caesar.sshcore.model;

import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/2 6:35 下午
 * @Version 1.0
 */
@Data
public class SshSessionMap {

    private static Map<String, SshSession> sshSessionMap = new HashedMap<>();

    public static void addSession(String sessionId, SshSession sshSession) {
        sshSessionMap.put(sessionId, sshSession);
    }

    public static SshSession getBySessionId(String sessionId) {
        return sshSessionMap.get(sessionId);
    }


}
