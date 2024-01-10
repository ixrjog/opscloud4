package com.baiyi.opscloud.datasource.aliyun.devops.driver;

import com.aliyun.sdk.service.devops20210625.AsyncClient;
import com.aliyun.sdk.service.devops20210625.models.ListProjectsRequest;
import com.aliyun.sdk.service.devops20210625.models.ListProjectsResponse;
import com.aliyun.sdk.service.devops20210625.models.ListProjectsResponseBody;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import com.baiyi.opscloud.datasource.aliyun.devops.client.AliyunDevopsClient;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.baiyi.opscloud.datasource.aliyun.devops.client.AliyunDevopsClient.MAX_RESULTS;

/**
 * @Author baiyi
 * @Date 2023/5/11 17:49
 * @Version 1.0
 */
public class AliyunDevopsProjectDriver {

    /**
     * 查询云效所有的项目
     * <p>
     * https://next.api.aliyun.com/api/devops/2021-06-25/ListProjects?spm=a2c4g.460498.0.i0&tab=DEMO
     *
     * @param regionId
     * @param devops
     * @return
     */
    public static List<ListProjectsResponseBody.Projects> listProjects(String regionId, AliyunDevopsConfig.Devops devops) {
        List<ListProjectsResponseBody.Projects> result = Lists.newArrayList();
        try (AsyncClient client = AliyunDevopsClient.buildAsyncClient(regionId, devops)) {
            ListProjectsRequest listProjectsRequest = ListProjectsRequest.builder()
                    .organizationId(devops.getOrganizationId())
                    .category("Project")
                    .maxResults(MAX_RESULTS)
                    .build();
            while (true) {
                CompletableFuture<ListProjectsResponse> response = client.listProjects(listProjectsRequest);
                result.addAll(response.get().getBody().getProjects());
                if (StringUtils.isBlank(response.get().getBody().getNextToken())) {
                    break;
                } else {
                    listProjectsRequest = ListProjectsRequest.builder()
                            .nextToken(response.get().getBody().getNextToken())
                            .organizationId(devops.getOrganizationId())
                            .category("Project")
                            .maxResults(MAX_RESULTS)
                            .build();
                }
            }
        } catch (Exception ignored) {
        }
        return result;
    }

}