package com.baiyi.opscloud.datasource.aliyun.ecs.driver;

import com.aliyuncs.ecs.model.v20140526.TagResourcesRequest;
import com.aliyuncs.ecs.model.v20140526.TagResourcesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/9/25 11:38
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunTagDriver {

    private final AliyunClient aliyunClient;

    /**
     * 资源打标 https://help.aliyun.com/zh/ecs/developer-reference/api-tagresources?spm=a2c4g.11186623.0.0.33ac2cd3EPOQHz
     *
     * @param regionId
     * @param aliyun
     * @param resourceType
     * @param resourceIds
     * @param tags
     * @return
     */
    public String tagResources(String regionId, AliyunConfig.Aliyun aliyun, String resourceType, List<String> resourceIds, List<TagResourcesRequest.Tag> tags) {
        try {
            TagResourcesRequest request = new TagResourcesRequest();
            request.setTags(tags);
            request.setResourceType(resourceType);
            request.setResourceIds(resourceIds);

            TagResourcesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            return response.getRequestId();

        } catch (ClientException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}