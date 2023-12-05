package com.baiyi.opscloud.facade.apollo;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.handler.ApolloReleaseHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/30 14:10
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class ApolloFacade {

    private final ApolloReleaseHandler apolloReleaseHandler;

    public HttpResult<Boolean> interceptRelease(ApolloParam.ReleaseEvent releaseEvent) {
        return apolloReleaseHandler.handleReleases(releaseEvent);
    }

}