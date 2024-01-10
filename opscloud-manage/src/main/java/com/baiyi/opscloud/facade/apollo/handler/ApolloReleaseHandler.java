package com.baiyi.opscloud.facade.apollo.handler;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.chain.*;
import com.baiyi.opscloud.facade.apollo.holder.ApolloConfigHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/11/10 17:17
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApolloReleaseHandler implements InitializingBean {

    // 拦截环境
    private final InterceptByEnvChainHandler interceptByEnvChainHandler;

    // 拦截应用
    private final InterceptByApplicationChainHandler interceptByApplicationChainHandler;

    // 发布白名单处理
    private final ReleaseApplicationWhiteListChainHandler releaseApplicationWhiteListChainHandler;

    // 工单发布
    private final ReleaseForWorkOrderChainHandler releaseWorkOrderReleaseChainHandler;

    // 拦截发布动作
    private final InterceptByActionChainHandler interceptByActionChainHandler;

    // 发布规则校验
    private final ReleaseRuleVerificationChainHandler releaseRuleVerificationChainHandler;

    private final ApolloConfigHolder apolloConfigHolder;

    public HttpResult<Boolean> handleReleases(ApolloParam.ReleaseEvent releaseEvent) {
        log.debug(JSONUtil.writeValueAsString(releaseEvent));
        Optional<ApolloConfig> optionalConfig = apolloConfigHolder.getConfigByToken(releaseEvent.getToken());
        return optionalConfig.map(apolloConfig -> interceptByEnvChainHandler.handleRequest(releaseEvent, apolloConfig))
                .orElse(HttpResult.SUCCESS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 组装责任链
        interceptByEnvChainHandler.setNextHandler(interceptByApplicationChainHandler)
                .setNextHandler(releaseApplicationWhiteListChainHandler)
                .setNextHandler(releaseWorkOrderReleaseChainHandler)
                .setNextHandler(interceptByActionChainHandler)
                .setNextHandler(releaseRuleVerificationChainHandler);
    }

}