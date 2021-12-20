package com.baiyi.opscloud.sshserver.command.context;

import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.sshserver.command.event.base.EventContext;
import com.baiyi.opscloud.sshserver.command.kubernetes.base.PodContext;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/5 4:06 下午
 * @Version 1.0
 */
public class SessionCommandContext {

    private static final ThreadLocal<Map<Integer, Integer>> idMapper = new ThreadLocal<>();

    private static final ThreadLocal<Map<Integer, PodContext>> podMapper = new ThreadLocal<>();

    private static final ThreadLocal<Map<Integer, EventContext>> eventMapper = new ThreadLocal<>();

    private static final ThreadLocal<ServerParam.UserPermissionServerPageQuery> serverQuery = new ThreadLocal<>();

    public static void setIdMapper(Map<Integer, Integer> param) {
        idMapper.set(param);
    }

    public static Map<Integer, Integer> getIdMapper() {
        return idMapper.get();
    }

    public static void setPodMapper(Map<Integer, PodContext> param) { podMapper.set(param);
    }

    public static Map<Integer, PodContext> getPodMapper() {
        return podMapper.get();
    }

    public static void setEventMapper(Map<Integer, EventContext> param) { eventMapper.set(param);
    }

    public static Map<Integer, EventContext> getEventMapper() {
        return eventMapper.get();
    }

    public static void setServerQuery(ServerParam.UserPermissionServerPageQuery param) {
        serverQuery.set(param);
    }

    public static ServerParam.UserPermissionServerPageQuery getServerQuery() {
        return serverQuery.get();
    }


}
