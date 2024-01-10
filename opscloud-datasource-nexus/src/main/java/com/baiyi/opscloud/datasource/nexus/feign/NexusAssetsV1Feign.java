package com.baiyi.opscloud.datasource.nexus.feign;

import com.baiyi.opscloud.datasource.nexus.entity.NexusAsset;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2021/10/15 2:56 下午
 * @Version 1.0
 */
public interface NexusAssetsV1Feign {

    @RequestLine("GET /service/rest/v1/assets?repository={repository}")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    NexusAsset.Assets listAssets(@Param("authBasic") String authBasic,
                                 @Param("repository") String repository);

    @RequestLine("GET /service/rest/v1/assets?repository={repository}&continuationToken={continuationToken}")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    NexusAsset.Assets listAssets(@Param("authBasic") String authBasic,
                                 @Param("repository") String repository,
                                 @Param("continuationToken") String continuationToken);


    @RequestLine("GET /service/rest/v1/assets/{id}")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    NexusAsset.Assets get(@Param("authBasic") String authBasic,
                                 @Param("id") String id);

}