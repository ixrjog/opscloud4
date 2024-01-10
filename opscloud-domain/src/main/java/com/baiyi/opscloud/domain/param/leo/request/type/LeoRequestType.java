package com.baiyi.opscloud.domain.param.leo.request.type;

/**
 * @Author baiyi
 * @Date 2022/11/23 15:34
 * @Version 1.0
 */
public enum LeoRequestType {

    // Build
    LOGIN,
    SUBSCRIBE_LEO_JOB,
    SUBSCRIBE_LEO_BUILD,
    QUERY_LEO_BUILD_CONSOLE_STREAM,
    // Deploy
    SUBSCRIBE_LEO_DEPLOY,
    SUBSCRIBE_LEO_DEPLOY_DETAILS,
    SUBSCRIBE_LEO_DEPLOYMENT_VERSION_DETAILS,
    // 认证失败
    AUTHENTICATION_FAILURE

}