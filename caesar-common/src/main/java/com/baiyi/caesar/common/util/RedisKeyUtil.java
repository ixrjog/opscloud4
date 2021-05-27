package com.baiyi.caesar.common.util;

import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:55 下午
 * @Version 1.0
 */
public class RedisKeyUtil {

    public static String getMyServerTreeKey(int userId, String uuid) {
        return Joiner.on(":").join("serverTree", "userId", userId, "uuid", uuid);
    }

    public static String getJobBuildKey(int buildId) {
        return Joiner.on(":").join("jobBuild", "buildId", buildId);
    }

    public static String getJobBuildAbortKey(int buildId) {
        return Joiner.on(":").join("abort", "jobBuild", "buildId", buildId);
    }

    public static String getJobDeploymentKey(int buildId) {
        return Joiner.on(":").join("jobDeployment", "buildId", buildId);
    }

}
