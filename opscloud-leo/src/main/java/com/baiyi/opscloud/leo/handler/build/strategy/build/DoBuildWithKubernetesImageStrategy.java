package com.baiyi.opscloud.leo.handler.build.strategy.build;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.constants.BuildTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.strategy.build.base.BaseDoBuildStrategy;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/4/26 11:32
 * @Version 1.0
 */
@Slf4j
@Component
public class DoBuildWithKubernetesImageStrategy extends BaseDoBuildStrategy {

    @Override
    protected void handlePostProcessDict(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig, LeoJob leoJob, Map<String, String> dict) {
        List<LeoBaseModel.Parameter> jobParameters = buildJobParameters(buildConfig);
        Map<String, String> paramMap = toParamMap(jobParameters);
        if (paramMap.containsKey(BuildDictConstants.REGISTRY_URL.getKey())) {
            dict.put(BuildDictConstants.REGISTRY_URL.getKey(), paramMap.get(BuildDictConstants.REGISTRY_URL.getKey()));
        } else {
            LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);
            final String registryUrl = Optional.ofNullable(jobConfig)
                    .map(LeoJobModel.JobConfig::getJob)
                    .map(LeoJobModel.Job::getCr)
                    .map(LeoJobModel.CR::getInstance)
                    .map(LeoJobModel.CRInstance::getUrl)
                    .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->cr->instance->url"));
            dict.put(BuildDictConstants.REGISTRY_URL.getKey(), registryUrl);
        }
        // 准备变量
        final String registryUrl = dict.get(BuildDictConstants.REGISTRY_URL.getKey());
        final String commitId = dict.get(BuildDictConstants.COMMIT_ID.getKey());
        final String buildNumber = dict.get(BuildDictConstants.BUILD_NUMBER.getKey());
        final String envName = dict.get(BuildDictConstants.ENV.getKey());
        final String project = dict.get(BuildDictConstants.PROJECT.getKey());
        /*
         * example: 460e7585-19
         */
        final String imageTag = Joiner.on("-").join(commitId, buildNumber);
        /*
         * example: aliyun-cr-uk.example.com/daily/merchant-rss:460e7585-19
         */
        dict.put(BuildDictConstants.IMAGE.getKey(), StringFormatter.arrayFormat("{}/{}/{}:{}", registryUrl, envName, project, imageTag));
        dict.put(BuildDictConstants.IMAGE_TAG.getKey(), imageTag);
    }

    @Override
    public String getBuildType() {
        return BuildTypeConstants.KUBERNETES_IMAGE;
    }

}