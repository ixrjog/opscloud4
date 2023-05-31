package com.baiyi.opscloud.datasource.apollo.client;

import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;

/**
 * @Author baiyi
 * @Date 2023/5/29 17:44
 * @Version 1.0
 */
public class ApolloClient {

    public static ApolloOpenApiClient build(ApolloConfig.Apollo apollo) {
        return ApolloOpenApiClient.newBuilder()
                .withPortalUrl(apollo.getPortal().getUrl())
                .withToken(apollo.getPortal().getToken())
                .build();
    }

}
