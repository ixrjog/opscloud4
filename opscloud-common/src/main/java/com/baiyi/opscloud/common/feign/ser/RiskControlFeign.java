package com.baiyi.opscloud.common.feign.ser;

import com.baiyi.opscloud.common.feign.request.RiskControlRequest;
import com.baiyi.opscloud.common.feign.response.MgwCoreResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author 修远
 * @Date 2023/7/5 1:35 PM
 * @Since 1.0
 */
public interface RiskControlFeign {

    @RequestLine("POST /api/{applicationName}/v1/ser-reload")
    @Headers({"Content-Type: application/json;charset=utf-8"})
    MgwCoreResponse<?> riskControlSerReload(@Param("applicationName") String applicationName, RiskControlRequest.SerLoader request);

}