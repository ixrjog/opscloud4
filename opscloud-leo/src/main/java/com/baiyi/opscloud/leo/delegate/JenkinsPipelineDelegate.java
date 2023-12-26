package com.baiyi.opscloud.leo.delegate;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.converter.JenkinsPipelineConverter;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.driver.BlueRestDriver;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/4/25 09:42
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JenkinsPipelineDelegate {

    private final BlueRestDriver blueRestDriver;

    private final DsConfigManager dsConfigManager;

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'OC4:V0:LEO:BUILD:PIPELINE:BID:' + #build.id")
    public LeoBuildVO.Pipeline getPipelineByCache(LeoBuildVO.Build build, LeoBuildModel.BuildConfig buildConfig) {
        return getPipeline(build,  buildConfig);
    }

    public LeoBuildVO.Pipeline getPipeline(LeoBuildVO.Build build, LeoBuildModel.BuildConfig buildConfig) {
        if (StringUtils.isNotBlank(build.getPipelineContent())) {
            try {
                return new GsonBuilder().create()
                        .fromJson(build.getPipelineContent(), LeoBuildVO.Pipeline.class);
            } catch (Exception e) {
                return LeoBuildVO.Pipeline.builder()
                        .nodes(Lists.newArrayList(LeoBuildVO.Node.INVALID))
                        .build();
            }
        } else {
            try {
                DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(buildConfig.getBuild().getJenkins().getInstance().getUuid());
                JenkinsConfig jenkinsConfig = dsConfigManager.build(dsConfig, JenkinsConfig.class);
                List<JenkinsPipeline.Node> nodes = blueRestDriver.getPipelineNodes(jenkinsConfig.getJenkins(), build.getBuildJobName(), String.valueOf(1));
                return LeoBuildVO.Pipeline.builder()
                        .nodes(JenkinsPipelineConverter.toLeoBuildNodes(nodes))
                        .build();
            } catch (Exception e) {
                return LeoBuildVO.Pipeline.builder()
                        .nodes(Lists.newArrayList(LeoBuildVO.Node.QUEUE))
                        .build();
            }
        }
    }

}