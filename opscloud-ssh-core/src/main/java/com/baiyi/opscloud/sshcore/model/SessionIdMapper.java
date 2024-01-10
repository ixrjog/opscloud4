package com.baiyi.opscloud.sshcore.model;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.sshd.common.io.IoSession;

import java.util.Map;
import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2021/7/21 11:34 上午
 * @Version 1.0
 */
public class SessionIdMapper {

    /**
     * SshServer使用  将ioSessionId转换为UUID
     */
    private static final Map<Long, String> MAPPER = new HashedMap<>();

    public static void put(IoSession ioSession) {
        MAPPER.put(ioSession.getId(), UUID.randomUUID().toString());
    }

    public static String getSessionId(IoSession ioSession) {
        return MAPPER.get(ioSession.getId());
    }

    public static void remove(IoSession ioSession) {
        MAPPER.remove(ioSession.getId());
    }

}