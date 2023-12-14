package com.baiyi.opscloud.datasource.aws.ecr.driver;

import com.amazonaws.services.ecr.model.*;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.ecr.service.AmazonEcrService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/16 16:42
 * @Version 1.0
 */
@Component
public class AmazonEcrRepositoryDriver {

    private static final int MAX_RESULTS = 1000;

    public List<Repository> describeRepositories(String regionId, AwsConfig.Aws config) {
        DescribeRepositoriesRequest request = new DescribeRepositoriesRequest();
        request.setMaxResults(MAX_RESULTS);
        List<Repository> repositories = Lists.newArrayList();
        while (true) {
            DescribeRepositoriesResult result = AmazonEcrService.buildAmazonECR(regionId, config).describeRepositories(request);
            repositories.addAll(result.getRepositories());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return repositories;
    }

    public Repository describeRepository(String regionId, AwsConfig.Aws config, String repositoryName) {
        List<Repository> repositories = describeRepositories(regionId, config, Lists.newArrayList(repositoryName));
        if (CollectionUtils.isEmpty(repositories))
            return null;
        return repositories.getFirst();
    }

    /**
     * https://docs.aws.amazon.com/AmazonECR/latest/APIReference/API_DescribeRepositories.html
     *
     * @param regionId
     * @param config
     * @param repositoryNames
     * @return
     */
    public List<Repository> describeRepositories(String regionId, AwsConfig.Aws config, List<String> repositoryNames) {
        DescribeRepositoriesRequest request = new DescribeRepositoriesRequest();
        request.setRepositoryNames(repositoryNames);
        List<Repository> repositories = Lists.newArrayList();
        while (true) {
            DescribeRepositoriesResult result = AmazonEcrService.buildAmazonECR(regionId, config).describeRepositories(request);
            repositories.addAll(result.getRepositories());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return repositories;
    }

    public Repository createRepository(String regionId, AwsConfig.Aws config, String repositoryName) {
        CreateRepositoryRequest request = new CreateRepositoryRequest();
        request.setRepositoryName(repositoryName);
        CreateRepositoryResult result = AmazonEcrService.buildAmazonECR(regionId, config).createRepository(request);
        return result.getRepository();
    }

}
