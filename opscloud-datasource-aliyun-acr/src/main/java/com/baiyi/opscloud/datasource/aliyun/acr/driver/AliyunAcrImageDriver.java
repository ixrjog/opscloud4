package com.baiyi.opscloud.datasource.aliyun.acr.driver;

import com.aliyuncs.cr.model.v20181201.GetRepoTagRequest;
import com.aliyuncs.cr.model.v20181201.GetRepoTagResponse;
import com.aliyuncs.cr.model.v20181201.ListRepoTagRequest;
import com.aliyuncs.cr.model.v20181201.ListRepoTagResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.base.BaseAliyunAcrDriver;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/17 13:32
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunAcrImageDriver extends BaseAliyunAcrDriver {

    /**
     * 查询仓库镜像
     *
     * @param regionId
     * @param aliyun
     * @param instanceId
     * @param repoId
     * @return
     * @throws ClientException
     */
    public List<ListRepoTagResponse.ImagesItem> listImage(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String repoId) throws ClientException {
        ListRepoTagRequest request = new ListRepoTagRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoId(repoId);
        request.setPageSize(BaseAliyunAcrDriver.Query.PAGE_SIZE);
        int total = 0;
        int pageNo = 1;

        List<ListRepoTagResponse.ImagesItem> imagesItems = Lists.newArrayList();
        while (total == 0 || total == imagesItems.size()) {
            request.setPageNo(pageNo);
            ListRepoTagResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            List<ListRepoTagResponse.ImagesItem> nowData = response.getImages();
            if (CollectionUtils.isEmpty(nowData)) {
                break;
            } else {
                imagesItems.addAll(nowData);
            }
            if (total == 0) {
                total = Integer.parseInt(response.getTotalCount());
            }
            pageNo++;
        }
        return imagesItems;
    }

    /**
     * 查询仓库镜像
     *
     * @param regionId
     * @param aliyun
     * @param instanceId
     * @param repoId
     * @param size
     * @return
     * @throws ClientException
     */
    public List<ListRepoTagResponse.ImagesItem> listImage(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String repoId, int size) throws ClientException {
        ListRepoTagRequest request = new ListRepoTagRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoId(repoId);
        request.setPageSize(Math.min(size, Query.PAGE_SIZE));
        ListRepoTagResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getImages();
    }

    public GetRepoTagResponse getImage(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String repoId, String tag) throws ClientException {
        GetRepoTagRequest request = new GetRepoTagRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoId(repoId);
        request.setTag(tag);
        return aliyunClient.getAcsResponse(regionId, aliyun, request);
    }

}