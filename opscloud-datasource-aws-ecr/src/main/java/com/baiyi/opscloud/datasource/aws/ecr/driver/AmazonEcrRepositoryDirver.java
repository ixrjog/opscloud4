package com.baiyi.opscloud.datasource.aws.ecr.driver;

import com.amazonaws.services.ecr.model.DescribeRepositoriesRequest;
import com.amazonaws.services.ecr.model.DescribeRepositoriesResult;
import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.ecr.service.AmazonEcrService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/8/16 16:42
 * @Version 1.0
 */
@Component
public class AmazonEcrRepositoryDirver {

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

}
