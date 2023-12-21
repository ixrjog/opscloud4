package com.baiyi.opscloud.facade.apollo.chain;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.handler.BaseApolloReleaseChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/11/10 17:46
 * @Version 1.0
 */
@Slf4j
@Component
public class ReleaseApplicationWhiteListChainHandler extends BaseApolloReleaseChainHandler {

    @Override
    protected HttpResult<Boolean> handle(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        // 跳过白名单
        List<String> whiteList = Optional.of(apolloConfig.getApollo())
                .map(ApolloConfig.Apollo::getPortal)
                .map(ApolloConfig.Portal::getRelease)
                .map(ApolloConfig.Release::getInterceptor)
                .map(ApolloConfig.Interceptor::getWhiteList)
                .orElse(Collections.emptyList());
        if (whiteList.stream().anyMatch(e -> releaseEvent.getAppId().equals(e))) {
            // 白名单通过是否需要发送通知 ？
            return HttpResult.SUCCESS;
        }
        return PASS_AND_DO_NEXT;
    }

}