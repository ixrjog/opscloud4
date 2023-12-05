package com.baiyi.opscloud.facade.apollo.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.constants.enums.ApolloReleaseActionEnum;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.ApolloFacade;
import com.baiyi.opscloud.facade.apollo.messenger.ApolloReleaseMessenger;
import com.baiyi.opscloud.service.application.ApplicationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;


class ApolloFacadeImplTest extends BaseUnit {

    @Resource
    private ApolloFacade apolloFacade;

    @Resource
    private DsConfigManager dsConfigManager;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApolloReleaseMessenger apolloReleaseMessenger;

    private static final String CLASS_A = "leo-demo";

    private static final String CLASS_B = "account-management";

    @Test
    void test() {
        ApolloParam.ReleaseEvent releaseEvent = ApolloParam.ReleaseEvent.builder()
                .appId(CLASS_A)
                .env("PROD")
                .clusterName("default")
                .username("baiyi")
                .namespaceName("application")
                .branchName("by1000002")
                .token("13f731d2-822b-e554-3a19-ba96f9189908")
                .isGray(false)
                .action(ApolloReleaseActionEnum.GRAY_RELEASE.name())
                .build();
        HttpResult<Boolean> result = apolloFacade.interceptRelease(releaseEvent);
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
        ApolloConfig apolloConfig = dsConfigManager.build(dsConfigManager.getConfigById(78), ApolloConfig.class);
        Application application = applicationService.getByName(CLASS_A);
        apolloReleaseMessenger.notify(apolloConfig, releaseEvent, 99999, application);
    }

}