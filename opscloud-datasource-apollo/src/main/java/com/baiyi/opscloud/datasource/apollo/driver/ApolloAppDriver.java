package com.baiyi.opscloud.datasource.apollo.driver;

import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.datasource.apollo.client.ApolloClient;
import com.ctrip.framework.apollo.openapi.dto.OpenAppDTO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/5/29 18:11
 * @Version 1.0
 */
public class ApolloAppDriver {

    public static List<OpenAppDTO> getAllApps(ApolloConfig.Apollo apollo) {
        return ApolloClient.build(apollo).getAllApps();
    }

    public static List<OpenAppDTO> getAuthorizedApps(ApolloConfig.Apollo apollo) {
        return ApolloClient.build(apollo).getAuthorizedApps();
    }

}
