package com.baiyi.opscloud.sshserver.command.context;

import com.baiyi.opscloud.domain.param.server.ServerParam;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/5 4:06 下午
 * @Version 1.0
 */
public class SessionCommandContext {

    private static ThreadLocal<Map<Integer, Integer>> idMapper = new ThreadLocal<>();
    private static ThreadLocal<ServerParam.UserPermissionServerPageQuery> serverQuery = new ThreadLocal<>();


    public static void setIdMapper(Map<Integer, Integer> param) {
        idMapper.set(param);
    }

    public static Map<Integer, Integer> getIdMapper() {
        return idMapper.get();
    }

    public static void setServerQuery(ServerParam.UserPermissionServerPageQuery param) {
        serverQuery.set(param);
    }

    public static ServerParam.UserPermissionServerPageQuery getServerQuery() {
        return serverQuery.get();
    }


}
