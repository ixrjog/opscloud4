package com.baiyi.opscloud.facade.apollo.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.ApolloFacade;
import com.baiyi.opscloud.service.application.ApplicationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;


class ApolloFacadeImplTest extends BaseUnit {

    @Resource
    private ApolloFacade apolloFacade;

    @Resource
    private DsConfigHelper dsConfigHelper;

    @Resource
    private ApplicationService applicationService;


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


    @Test
    void test2() {
        ApolloParam.ReleaseEvent releaseEvent = ApolloParam.ReleaseEvent.builder()
                .appId(CLASS_A)
                .env("PROD")
                .clusterName("default")
                .namespaceName("application")
                .username("baiyi")
                .isGray(false)
                .token("13f731d2-822b-e554-3a19-ba96f9189908")
                .build();
        ApolloConfig apolloConfig = dsConfigHelper.build(dsConfigHelper.getConfigById(78), ApolloConfig.class);
        Application application = applicationService.getByName(CLASS_A);
        apolloFacade.notify(apolloConfig, releaseEvent, 99999, application);
    }

}