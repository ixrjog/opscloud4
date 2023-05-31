package com.baiyi.opscloud.facade.apollo;

import com.baiyi.opscloud.common.HttpResult;
import com.baiyi.opscloud.domain.param.apollo.ApolloParam;

/**
 * @Author baiyi
 * @Date 2023/5/30 14:10
 * @Version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ApolloFacade {

    /**
     * Apollo发布拦截
     * @param releaseEvent
     * @return
     */
    HttpResult interceptRelease(ApolloParam.ReleaseEvent releaseEvent);

}
