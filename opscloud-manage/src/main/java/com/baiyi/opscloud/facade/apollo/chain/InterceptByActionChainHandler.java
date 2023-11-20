package com.baiyi.opscloud.facade.apollo.chain;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.common.constants.enums.ApolloReleaseActionEnum;
import com.baiyi.opscloud.common.datasource.ApolloConfig;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import com.baiyi.opscloud.facade.apollo.handler.BaseApolloReleaseChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

/**
 * @Author baiyi
 * @Date 2023/11/10 17:48
 * @Version 1.0
 */
@Slf4j
@Component
public class InterceptByActionChainHandler extends BaseApolloReleaseChainHandler {

    @Override
    protected HttpResult<Boolean> handle(ApolloParam.ReleaseEvent releaseEvent, ApolloConfig apolloConfig) {
        // 判断动作，禁止全量发布
        if (StringUtils.isNotBlank(releaseEvent.getAction()) && ApolloReleaseActionEnum.RELEASE.name().equalsIgnoreCase(releaseEvent.getAction())) {
            return HttpResult.<Boolean>builder()
                    .success(false)
                    .code(SC_BAD_REQUEST)
                    .msg("当前应用禁止全量发布配置，请使用灰度发布！")
                    .build();
        }
        // 灰度发布，全部放行
        if (StringUtils.isNotBlank(releaseEvent.getAction()) && ApolloReleaseActionEnum.GRAY_RELEASE.name().equalsIgnoreCase(releaseEvent.getAction())) {
            return notify(apolloConfig, releaseEvent);
        }

        return PASS_AND_DO_NEXT;
    }

}
