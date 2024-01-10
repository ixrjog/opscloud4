package com.baiyi.opscloud.datasource.nexus.feign;

import com.baiyi.opscloud.datasource.nexus.entity.NexusComponent;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2023/4/23 11:16
 * @Version 1.0
 */
public interface NexusComponentsV1Feign {

    @RequestLine("GET /service/rest/v1/components?{id}")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    NexusComponent.Component getComponent(@Param("authBasic") String authBasic, @Param("id") String id);

}