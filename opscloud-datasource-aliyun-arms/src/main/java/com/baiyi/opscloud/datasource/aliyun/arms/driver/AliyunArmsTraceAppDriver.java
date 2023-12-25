package com.baiyi.opscloud.datasource.aliyun.arms.driver;

import com.aliyun.sdk.service.arms20190808.AsyncClient;
import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsRequest;
import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsResponse;
import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsResponseBody;
import com.baiyi.opscloud.common.datasource.AliyunArmsConfig;
import com.baiyi.opscloud.datasource.aliyun.arms.client.AliyunArmsClient;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author baiyi
 * @Date 2023/6/27 10:14
 * @Version 1.0
 */
public class AliyunArmsTraceAppDriver {

    public static List<ListTraceAppsResponseBody.TraceApps> listTraceApps(String regionId, AliyunArmsConfig.Arms arms) {
        List<ListTraceAppsResponseBody.TraceApps> result = Lists.newArrayList();
        try (AsyncClient client = AliyunArmsClient.buildAsyncClient(regionId, arms)) {
            ListTraceAppsRequest listProjectsRequest = ListTraceAppsRequest.builder()
                    .regionId(regionId)
                    .build();

            CompletableFuture<ListTraceAppsResponse> response = client.listTraceApps(listProjectsRequest);
            result = response.get().getBody().getTraceApps();
        } catch (Exception ignored) {
        }
        return result;
    }

}