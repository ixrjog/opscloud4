package com.baiyi.opscloud.facade.apollo.chain;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.handler.BaseApolloReleaseChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/11/10 17:39
 * @Version 1.0
 */
@Slf4j
@Component
public class InterceptByEnvChainHandler extends BaseApolloReleaseChainHandler {

    @Override
    protected HttpResult<Boolean> handle(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        // 需要拦截的环境
        List<String> envs = Optional.of(apolloConfig.getApollo())
                .map(ApolloConfig.Apollo::getPortal)
                .map(ApolloConfig.Portal::getRelease)
                .map(ApolloConfig.Release::getInterceptor)
                .map(ApolloConfig.Interceptor::getEnvs)
                .orElse(Collections.emptyList());
        // 环境配置不存在
        if (CollectionUtils.isEmpty(envs)) {
            log.debug("Interceptor environment configuration is empty");
            return HttpResult.SUCCESS;
        }
        // 当前环境不拦截
        if (envs.stream().noneMatch(e -> e.equals(releaseEvent.getEnv()))) {
            return HttpResult.SUCCESS;
        }
        return PASS_AND_DO_NEXT;
    }

}
