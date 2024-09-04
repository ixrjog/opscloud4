package com.baiyi.opscloud.facade.apollo.consumer;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.core.entity.InterceptRelease;
import com.baiyi.opscloud.datasource.apollo.provider.ApolloInterceptReleaseProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/11/13 13:45
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ApolloEventAssetConsumer {

    private final DsInstanceService dsInstanceService;

    private final ApolloInterceptReleaseProvider apolloInterceptReleaseProvider;

    private final ApolloReleaseEventPublisher apolloReleaseEventPublisher;

    private final UserService userService;

    /**
     * 记录
     *
     * @param apolloConfig
     * @param releaseEvent
     * @param httpResult
     * @param ticketId
     */
    public void consume(ApolloConfig apolloConfig, ApolloParam.ReleaseEvent releaseEvent, HttpResult<Boolean> httpResult, Integer ticketId) {
        DatasourceInstance datasourceInstance = dsInstanceService.getByConfigId(apolloConfig.getConfigId());
        if (datasourceInstance == null) {
            return;
        }
        User user = userService.getByUsername(releaseEvent.getUsername());
        String email = user != null? user.getEmail() : "";
        InterceptRelease.Event event = InterceptRelease.Event.builder()
                .url(Optional.of(apolloConfig).map(ApolloConfig::getApollo).map(ApolloConfig.Apollo::getPortal).map(ApolloConfig.Portal::getUrl).orElse(""))
                .appId(releaseEvent.getAppId())
                .env(releaseEvent.getEnv())
                .clusterName(releaseEvent.getClusterName())
                .namespaceName(releaseEvent.getNamespaceName())
                .isGray(releaseEvent.getIsGray())
                .username(releaseEvent.getUsername())
                .email(email)
                .branchName(releaseEvent.getBranchName())
                .releaseTitle(releaseEvent.getReleaseTitle())
                .success(httpResult.isSuccess())
                .code(httpResult.getCode())
                .msg(httpResult.getMsg())
                .ticketId(ticketId)
                .action(releaseEvent.getAction())
                .build();
        // 写入资产
        apolloInterceptReleaseProvider.pullAsset(datasourceInstance.getId(), event);
        apolloReleaseEventPublisher.publish(event);
    }

}
