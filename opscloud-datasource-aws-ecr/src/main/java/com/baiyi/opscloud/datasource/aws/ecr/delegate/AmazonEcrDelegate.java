package com.baiyi.opscloud.datasource.aws.ecr.delegate;

import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aws.ecr.driver.AmazonEcrRepositoryDriver;
import com.baiyi.opscloud.datasource.aws.ecr.entity.AmazonEcr;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/8/16 17:42
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AmazonEcrDelegate {

    private final AmazonEcrRepositoryDriver amazonEcrRepositoryDriver;

    @Retryable(retryFor = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1.5))
    public List<AmazonEcr.Repository> listRepository(AwsConfig.Aws config) {
        if (CollectionUtils.isEmpty(config.getRegionIds())) {
            return amazonEcrRepositoryDriver.describeRepositories(config.getRegionId(), config).stream().map(e ->
                    toRepository(config.getRegionId(), e)
            ).collect(Collectors.toList());
        }
        List<AmazonEcr.Repository> repositories = Lists.newArrayList();
        for (String regionId : config.getRegionIds()) {
            repositories.addAll(amazonEcrRepositoryDriver.describeRepositories(regionId, config).stream().map(e ->
                    toRepository(regionId, e)
            ).toList());
        }
        return repositories;
    }

    private AmazonEcr.Repository toRepository(String regionId, Repository repository) {
        AmazonEcr.Repository repo = BeanCopierUtil.copyProperties(repository, AmazonEcr.Repository.class);
        repo.setRegionId(regionId);
        return repo;
    }

}
