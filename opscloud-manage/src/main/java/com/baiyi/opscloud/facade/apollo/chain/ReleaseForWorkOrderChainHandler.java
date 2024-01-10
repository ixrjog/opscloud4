package com.baiyi.opscloud.facade.apollo.chain;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.common.holder.WorkOrderApolloReleaseHolder;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.model.WorkOrderToken;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.handler.BaseApolloReleaseChainHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/11/10 18:32
 * @Version 1.0
 */
@Slf4j
@Component
public class ReleaseForWorkOrderChainHandler extends BaseApolloReleaseChainHandler {

    @Resource
    private WorkOrderApolloReleaseHolder workOrderApolloReleaseHolder;

    @Override
    protected HttpResult<Boolean> handle(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        int ticketId = getWorkOrderTicketId(releaseEvent);
        if (ticketId == 0) {
            return PASS_AND_DO_NEXT;
        } else {
            return notify(apolloConfig, releaseEvent, ticketId);
        }
    }

    public Integer getWorkOrderTicketId(ApolloParam.ReleaseEvent releaseEvent) {
        Application application = applicationService.getByName(releaseEvent.getAppId());
        if (application == null) {
            return NO_WORK_ORDER_ID;
        }
        if (workOrderApolloReleaseHolder.hasKey(application.getId())) {
            WorkOrderToken.ApolloReleaseToken token = workOrderApolloReleaseHolder.getToken(application.getId());
            return token.getTicketId();
        }
        return NO_WORK_ORDER_ID;
    }

}