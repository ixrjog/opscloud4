package com.baiyi.opscloud.datasource.nacos.feign;

import com.baiyi.opscloud.datasource.nacos.entity.NacosCluster;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/11/12 10:18 上午
 * @Version 1.0
 */
public interface NacosClusterV1Feign {

    @RequestLine("GET /nacos/v1/core/cluster/nodes?" +
            "withInstances={withInstances}" +
            "&pageNo={pageNo}" +
            "&pageSize={pageSize}" +
            "&keyword={keyword}" +
            "&namespaceId={namespaceId}" +
            "&accessToken={accessToken}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    NacosCluster.NodesResponse listNodes(@Param("withInstances") Boolean withInstances,
                                         @Param("pageNo") Integer pageNo,
                                         @Param("pageSize") Integer pageSize,
                                         @Param("keyword") String keyword,
                                         @Param("namespaceId") String namespaceId,
                                         @Param("accessToken") String accessToken
    );

}