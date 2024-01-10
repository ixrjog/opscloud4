package com.baiyi.opscloud.datasource.aliyun.devops.driver;

import com.aliyun.sdk.service.devops20210625.AsyncClient;
import com.aliyun.sdk.service.devops20210625.models.ListSprintsRequest;
import com.aliyun.sdk.service.devops20210625.models.ListSprintsResponse;
import com.aliyun.sdk.service.devops20210625.models.ListSprintsResponseBody;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import com.baiyi.opscloud.datasource.aliyun.devops.client.AliyunDevopsClient;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.baiyi.opscloud.datasource.aliyun.devops.client.AliyunDevopsClient.MAX_RESULTS;

/**
 * @Author baiyi
 * @Date 2023/5/12 10:19
 * @Version 1.0
 */
public class AliyunDevopsSprintsDriver {

    /**
     * 查询迭代
     *
     * @param regionId
     * @param devops
     * @return
     */
    public static List<ListSprintsResponseBody.Sprints> listSprints(String regionId, AliyunDevopsConfig.Devops devops) {
        List<ListSprintsResponseBody.Sprints> result = Lists.newArrayList();
        try (AsyncClient client = AliyunDevopsClient.buildAsyncClient(regionId, devops)) {

            ListSprintsRequest request = ListSprintsRequest.builder()
                    .maxResults(MAX_RESULTS)
                    .organizationId(devops.getOrganizationId())
                    .build();
            while (true) {
                CompletableFuture<ListSprintsResponse> response = client.listSprints(request);
                result.addAll(response.get().getBody().getSprints());
                if (StringUtils.isBlank(response.get().getBody().getNextToken())) {
                    break;
                } else {
                    request = ListSprintsRequest.builder()
                            .nextToken(response.get().getBody().getNextToken())
                            .organizationId(devops.getOrganizationId())
                            .maxResults(MAX_RESULTS)
                            .build();
                }
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * 查询迭代
     *
     * @param regionId
     * @param devops
     * @param spaceIdentifier 同projectId
     * @return
     */
    public static List<ListSprintsResponseBody.Sprints> listSprints(String regionId, AliyunDevopsConfig.Devops devops, String spaceType, String spaceIdentifier) {
        List<ListSprintsResponseBody.Sprints> result = Lists.newArrayList();
        try(AsyncClient client = AliyunDevopsClient.buildAsyncClient(regionId, devops)) {

            ListSprintsRequest request = ListSprintsRequest.builder()
                    .organizationId(devops.getOrganizationId())
                    .spaceType(spaceType)
                    .spaceIdentifier(spaceIdentifier)
                    .maxResults(MAX_RESULTS)
                    .build();
            while (true) {
                CompletableFuture<ListSprintsResponse> response = client.listSprints(request);
                result.addAll(response.get().getBody().getSprints());
                if (StringUtils.isBlank(response.get().getBody().getNextToken())) {
                    break;
                } else {
                    request = ListSprintsRequest.builder()
                            .nextToken(response.get().getBody().getNextToken())
                            .organizationId(devops.getOrganizationId())
                            .spaceType(spaceType)
                            .spaceIdentifier(spaceIdentifier)
                            .maxResults(MAX_RESULTS)
                            .build();
                }
            }
        } catch (Exception ignored) {
        }
        return result;
    }

}