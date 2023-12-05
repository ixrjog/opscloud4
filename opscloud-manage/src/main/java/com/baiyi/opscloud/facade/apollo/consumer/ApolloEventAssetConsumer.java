package com.baiyi.opscloud.facade.apollo.consumer;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.datasource.apollo.entity.InterceptRelease;
import com.baiyi.opscloud.datasource.apollo.provider.ApolloInterceptReleaseProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/11/13 13:45
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ApolloEventAssetConsumer {

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private ApolloInterceptReleaseProvider apolloInterceptReleaseProvider;

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
        InterceptRelease.Event event = InterceptRelease.Event.builder()
                .appId(releaseEvent.getAppId())
                .env(releaseEvent.getEnv())
                .clusterName(releaseEvent.getClusterName())
                .namespaceName(releaseEvent.getNamespaceName())
                .isGray(releaseEvent.getIsGray())
                .username(releaseEvent.getUsername())
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
    }

}
