package com.baiyi.opscloud.facade.apollo.feign;

import com.baiyi.opscloud.domain.param.apollo.ApolloParam;
import feign.Headers;
import feign.RequestLine;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/8 10:43
 * &#064;Version 1.0
 */
public interface DevopsFeign {

    @RequestLine("POST /apollo/release/hook")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    Object callHook(ApolloParam.ReleaseEvent releaseEvent);

}
