package com.baiyi.opscloud.datasource.aliyun.acr.driver;

import com.aliyuncs.cr.model.v20181201.ListRepositoryRequest;
import com.aliyuncs.cr.model.v20181201.ListRepositoryResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/12 10:35
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunAcrRepositoryDriver {

    private final AliyunClient aliyunClient;

    public interface Query {
        int PAGE_SIZE = 30;
        String REPO_STATUS_NORMAL = "NORMAL";
    }

    /**
     * @param regionId
     * @param aliyun
     * @param instanceId
     * @return
     */
    public List<AliyunAcr.Repository> listRepositories(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) {
        List<ListRepositoryResponse.RepositoriesItem> repositoriesItems = Lists.newArrayList();
        ListRepositoryRequest request = new ListRepositoryRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setRepoStatus(Query.REPO_STATUS_NORMAL);
        request.setPageSize(Query.PAGE_SIZE);
        int size = Query.PAGE_SIZE;
        int pageNo = 1;
        try {
            while (Query.PAGE_SIZE <= size) {
                request.setPageNo(pageNo);
                ListRepositoryResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
                repositoriesItems.addAll(response.getRepositories());
                size = response.getRepositories().size();
                pageNo++;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return toRepositories(repositoriesItems);
    }

    private List<AliyunAcr.Repository> toRepositories(List<ListRepositoryResponse.RepositoriesItem> repositoriesItems) {
        return BeanCopierUtil.copyListProperties(repositoriesItems, AliyunAcr.Repository.class);
    }

}
