package com.baiyi.opscloud.facade.apollo.handler;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.chain.ReleaseForWorkOrderChainHandler;
import com.baiyi.opscloud.facade.apollo.consumer.ApolloEventAssetConsumer;
import com.baiyi.opscloud.facade.apollo.messenger.ApolloReleaseMessenger;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2023/11/10 17:18
 * @Version 1.0
 */
@Slf4j
@Getter
public abstract class BaseApolloReleaseChainHandler {

    private BaseApolloReleaseChainHandler next;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private ApolloEventAssetConsumer apolloAssetConsumer;

    @Resource
    private ApolloReleaseMessenger apolloReleaseMessenger;

    @Resource
    protected ApplicationService applicationService;

    @Resource
    private ReleaseForWorkOrderChainHandler releaseForWorkOrderChainHandler;

    protected static final HttpResult<Boolean> PASS_AND_DO_NEXT = null;

    protected static final int NO_WORK_ORDER_ID = 0;

    public BaseApolloReleaseChainHandler setNextHandler(BaseApolloReleaseChainHandler next) {
       this.next = next;
       return this.next;
    }

    public HttpResult<Boolean> handleRequest(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        HttpResult<Boolean> httpResult = this.handle(releaseEvent, apolloConfig);
        // 当前链执行结束
        if (httpResult != null) {
            int ticketId = releaseForWorkOrderChainHandler.getWorkOrderTicketId(releaseEvent);
            this.consume(apolloConfig, releaseEvent, httpResult, ticketId);
            return httpResult;
        }
        // 下链执行
        if (getNext() != null) {
            return getNext().handleRequest(releaseEvent, apolloConfig);
        } else {
            return HttpResult.SUCCESS;
        }
    }

    /**
     * 抽象方法，具体实现
     *
     * @param releaseEvent
     * @param apolloConfig
     * @return
     */
    protected abstract HttpResult<Boolean> handle(final ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig);

    /**
     * 记录(无工单)
     *
     * @param apolloConfig
     * @param releaseEvent
     * @param httpResult
     * @param ticketId
     */
    private void consume(ApolloConfig apolloConfig, ApolloParam.ReleaseEvent releaseEvent, HttpResult<Boolean> httpResult, Integer ticketId) {
        apolloAssetConsumer.consume(apolloConfig, releaseEvent, httpResult, ticketId);
    }

    /**
     * Notify and Result
     *
     * @param apolloConfig
     * @param releaseEvent
     * @param ticketId
     * @return
     */
    protected HttpResult<Boolean> notify(ApolloConfig apolloConfig, ApolloParam.ReleaseEvent releaseEvent, Integer ticketId) {
        Application application = applicationService.getByName(releaseEvent.getAppId());
        apolloReleaseMessenger.notify(apolloConfig, releaseEvent, ticketId, application);
        return HttpResult.SUCCESS;
    }

    /**
     * Notify and Result
     *
     * @param apolloConfig
     * @param releaseEvent
     * @return
     */
    protected HttpResult<Boolean> notify(ApolloConfig apolloConfig, ApolloParam.ReleaseEvent releaseEvent) {
        this.notify(apolloConfig, releaseEvent, NO_WORK_ORDER_ID);
        return HttpResult.SUCCESS;
    }

}