package com.baiyi.opscloud.datasource.aliyun.acr.driver;

import com.aliyuncs.cr.model.v20181201.GetRepositoryRequest;
import com.aliyuncs.cr.model.v20181201.GetRepositoryResponse;
import com.aliyuncs.cr.model.v20181201.ListRepositoryRequest;
import com.aliyuncs.cr.model.v20181201.ListRepositoryResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.base.BaseAliyunAcrDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/12 10:35
 * @Version 1.0
 */
@Slf4j
@Component
public class AliyunAcrRepositoryDriver extends BaseAliyunAcrDriver {

    /**
     * @param regionId
     * @param aliyun
     * @param instanceId
     * @return
     */
    public List<ListRepositoryResponse.RepositoriesItem> listRepository(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        ListRepositoryRequest request = new ListRepositoryRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoStatus(Query.NORMAL);
        request.setPageSize(Query.PAGE_SIZE);
        int total = 0;
        int pageNo = 1;

        List<ListRepositoryResponse.RepositoriesItem> repositoriesItems = Lists.newArrayList();
        while (total == 0 || total == repositoriesItems.size()) {
            request.setPageNo(pageNo);
            ListRepositoryResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            List<ListRepositoryResponse.RepositoriesItem> nowData = response.getRepositories();
            if (CollectionUtils.isEmpty(nowData)) {
                break;
            } else {
                repositoriesItems.addAll(nowData);
            }
            if (total == 0) {
                total = Integer.parseInt(response.getTotalCount());
            }
            pageNo++;
        }
        return repositoriesItems;
    }

    public String getRepositoryId(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String repoNamespaceName, String repoName) throws ClientException {
        GetRepositoryRequest request = new GetRepositoryRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoNamespaceName(repoNamespaceName);
        request.setRepoName(repoName);
        GetRepositoryResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getRepoId();
    }

    private List<AliyunAcr.Repository> toRepositories(List<ListRepositoryResponse.RepositoriesItem> repositoriesItems) {
        return BeanCopierUtil.copyListProperties(repositoriesItems, AliyunAcr.Repository.class);
    }

}