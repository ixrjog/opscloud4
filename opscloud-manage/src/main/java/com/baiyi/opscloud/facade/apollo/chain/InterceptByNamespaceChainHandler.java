package com.baiyi.opscloud.facade.apollo.chain;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.common.util.MatchingUtil;
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
 * @Date 2023/12/20 16:42
 * @Version 1.0
 */
@Slf4j
@Component
public class InterceptByNamespaceChainHandler extends BaseApolloReleaseChainHandler {

    /**
     * 通配符`*`只能出现在首尾
     *
     * @param releaseEvent
     * @param apolloConfig
     * @return
     */
    @Override
    protected HttpResult<Boolean> handle(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        // 获取Namespace配置
        List<String> namespaces = Optional.of(apolloConfig.getApollo())
                .map(ApolloConfig.Apollo::getPortal)
                .map(ApolloConfig.Portal::getRelease)
                .map(ApolloConfig.Release::getInterceptor)
                .map(ApolloConfig.Interceptor::getNamespaces)
                .orElse(Collections.emptyList());

        if (CollectionUtils.isEmpty(namespaces)) {
            // 未配置命名空间，不拦截所有发布
            return HttpResult.SUCCESS;
        }

        for (String namespace : namespaces) {
            if (MatchingUtil.fuzzyMatching(releaseEvent.getNamespaceName(), namespace)) {
                return PASS_AND_DO_NEXT;
            }
        }
        return HttpResult.SUCCESS;
    }

}