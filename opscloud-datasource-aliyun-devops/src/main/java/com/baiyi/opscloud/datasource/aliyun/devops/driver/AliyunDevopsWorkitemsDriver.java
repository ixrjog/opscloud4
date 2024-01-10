package com.baiyi.opscloud.datasource.aliyun.devops.driver;

import com.aliyun.sdk.service.devops20210625.AsyncClient;
import com.aliyun.sdk.service.devops20210625.models.ListWorkitemsRequest;
import com.aliyun.sdk.service.devops20210625.models.ListWorkitemsResponse;
import com.aliyun.sdk.service.devops20210625.models.ListWorkitemsResponseBody;
import com.baiyi.opscloud.common.datasource.AliyunDevopsConfig;
import com.baiyi.opscloud.datasource.aliyun.devops.client.AliyunDevopsClient;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.baiyi.opscloud.datasource.aliyun.devops.client.AliyunDevopsClient.MAX_RESULTS_S;

/**
 * @Author baiyi
 * @Date 2023/5/12 10:48
 * @Version 1.0
 */
public class AliyunDevopsWorkitemsDriver {

    /**
     * 查询工作空间
     *
     * @param regionId
     * @param devops
     * @param spaceIdentifier 项目ID
     * @param spaceType       Project
     * @param category        工作项大类型，需求为Req，缺陷为Bug，任务为Task，风险为Risk
     * @return
     */
    public static List<ListWorkitemsResponseBody.Workitems> listWorkitems(String regionId, AliyunDevopsConfig.Devops devops,
                                                                          String spaceIdentifier,
                                                                          String spaceType,
                                                                          String category) {
        List<ListWorkitemsResponseBody.Workitems> result = Lists.newArrayList();
        try (AsyncClient client = AliyunDevopsClient.buildAsyncClient(regionId, devops)) {
            ListWorkitemsRequest request = ListWorkitemsRequest.builder()
                    .spaceIdentifier(spaceIdentifier)
                    .organizationId(devops.getOrganizationId())
                    .spaceType(spaceType)
                    .category(category)
                    .maxResults(MAX_RESULTS_S)
                    .build();
            while (true) {
                CompletableFuture<ListWorkitemsResponse> response = client.listWorkitems(request);
                result.addAll(response.get().getBody().getWorkitems());
                if (StringUtils.isBlank(response.get().getBody().getNextToken())) {
                    break;
                } else {
                    request = ListWorkitemsRequest.builder()
                            .nextToken(response.get().getBody().getNextToken())
                            .organizationId(devops.getOrganizationId())
                            .spaceIdentifier(spaceIdentifier)
                            .spaceType(spaceType)
                            .category(category)
                            .maxResults(MAX_RESULTS_S)
                            .build();
                }
            }
        } catch (Exception ignored) {
        }
        return result;
    }

}