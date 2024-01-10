package com.baiyi.opscloud.datasource.aliyun.acr.delegate;

import com.aliyuncs.cr.model.v20181201.ListRepositoryResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.acr.driver.AliyunAcrRepositoryDriver;
import com.baiyi.opscloud.datasource.aliyun.acr.entity.AliyunAcr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/15 17:45
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunAcrRepositoryDelegate {

    private final AliyunAcrRepositoryDriver aliyunAcrRepositoryDriver;

    public List<AliyunAcr.Repository> listRepository(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        List<ListRepositoryResponse.RepositoriesItem> repositoriesItems = aliyunAcrRepositoryDriver.listRepository(regionId, aliyun, instanceId);
        return toRepositories(repositoriesItems);
    }

    private List<AliyunAcr.Repository> toRepositories(List<ListRepositoryResponse.RepositoriesItem> repositoriesItems) {
        return BeanCopierUtil.copyListProperties(repositoriesItems, AliyunAcr.Repository.class);
    }

}