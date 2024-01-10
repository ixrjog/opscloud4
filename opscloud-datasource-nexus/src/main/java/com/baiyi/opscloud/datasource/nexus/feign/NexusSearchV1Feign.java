package com.baiyi.opscloud.datasource.nexus.feign;

import com.baiyi.opscloud.datasource.nexus.entity.NexusComponent;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @Author baiyi
 * @Date 2023/4/23 15:09
 * @Version 1.0
 */
public interface NexusSearchV1Feign {

    @RequestLine("GET /service/rest/v1/search?repository={repository}&group={group}&name={name}&version={version}")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    NexusComponent.Components search(@Param("authBasic") String authBasic,
                                     @Param("repository") String repository,
                                     @Param("group") String group,
                                     @Param("name") String name,
                                     @Param("version") String version);

    /**
     * 查询组件
     *
     * @param authBasic
     * @param repository
     * @param group
     * @param continuationToken
     * @return
     */
    @RequestLine("GET /service/rest/v1/search?repository={repository}&group={group}&name={name}&version={version}&continuationToken={continuationToken}")
    @Headers({"accept: application/json", "Authorization: Basic {authBasic}"})
    NexusComponent.Components search(@Param("authBasic") String authBasic,
                                     @Param("repository") String repository,
                                     @Param("group") String group,
                                     @Param("name") String name,
                                     @Param("version") String version,
                                     @Param("continuationToken") String continuationToken);

}