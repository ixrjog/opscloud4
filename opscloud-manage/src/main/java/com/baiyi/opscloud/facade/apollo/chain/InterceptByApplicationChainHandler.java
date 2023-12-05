package com.baiyi.opscloud.facade.apollo.chain;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.handler.BaseApolloReleaseChainHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/11/10 17:44
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class InterceptByApplicationChainHandler extends BaseApolloReleaseChainHandler {

    @Override
    protected HttpResult<Boolean> handle(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        // 判断是否是标准应用，若非标准应用则允许发布配置
        Application application = applicationService.getByName(releaseEvent.getAppId());
        if (application == null) {
            return HttpResult.SUCCESS;
        }
        return PASS_AND_DO_NEXT;
    }

}
