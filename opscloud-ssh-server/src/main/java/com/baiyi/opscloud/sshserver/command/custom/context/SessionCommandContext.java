package com.baiyi.opscloud.sshserver.command.custom.context;

import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.sshserver.command.custom.kubernetes.base.PodContext;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/5 4:06 下午
 * @Version 1.0
 */
public class SessionCommandContext {

    private static final ThreadLocal<Map<Integer, Integer>> ID_MAPPER = new ThreadLocal<>();

    public static void setIdMapper(Map<Integer, Integer> param) {
        ID_MAPPER.set(param);
    }

    public static Map<Integer, Integer> getIdMapper() {
        return ID_MAPPER.get();
    }

    private static final ThreadLocal<ServerParam.UserPermissionServerPageQuery> SERVER_QUERY = new ThreadLocal<>();

    public static void setServerQuery(ServerParam.UserPermissionServerPageQuery param) {
        SERVER_QUERY.set(param);
    }

    public static ServerParam.UserPermissionServerPageQuery getServerQuery() {
        return SERVER_QUERY.get();
    }

    private static final ThreadLocal<Map<Integer, PodContext>> POD_MAPPER = new ThreadLocal<>();

    public static Map<Integer, PodContext> getPodMapper() {
        return POD_MAPPER.get();
    }

    public static void setPodMapper(Map<Integer, PodContext> param) {
        POD_MAPPER.set(param);
    }

    public static void remove() {
        ID_MAPPER.remove();
        SERVER_QUERY.remove();
        POD_MAPPER.remove();
    }

}