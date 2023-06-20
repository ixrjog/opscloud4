package com.baiyi.opscloud.facade.apollo.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.ApolloFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;


class ApolloFacadeImplTest extends BaseUnit {

    @Resource
    private ApolloFacade apolloFacade;

    private static final String CLASS_A = "trade";

    private static final String CLASS_B = "account-management";

    @Test
    void test() {
        ApolloParam.ReleaseEvent releaseEvent = ApolloParam.ReleaseEvent.builder()
                .appId(CLASS_A)
                .env("PROD")
                .clusterName("default")
                .namespaceName("application")
                .token("13f731d2-822b-e554-3a19-ba96f9189908")
                .build();
        HttpResult<?> result = apolloFacade.interceptRelease(releaseEvent);
        print(result);
    }

}