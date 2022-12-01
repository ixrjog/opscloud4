package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.annotation.RuntimeWrapper;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.converter.JenkinsPipelineConverter;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.driver.BlueRestDriver;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/24 17:46
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoBuildResponsePacker {

    private final BlueRestDriver blueRestDriver;

    private final DsConfigHelper dsConfigHelper;

    private final LeoBuildImageService leoBuildImageService;

    //  @EnvWrapper(extend = true)
    @AgoWrapper(extend = true)
    @RuntimeWrapper(extend = true)
    public void wrap(LeoBuildVO.Build build) {
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(build.getBuildConfig());
        build.setBuildDetails(buildConfig);
        LeoBuildVO.Pipeline pipeline = getPipeline(build, buildConfig);
        build.setPipeline(pipeline);
    }

    private LeoBuildVO.Pipeline getPipeline(LeoBuildVO.Build build, LeoBuildModel.BuildConfig buildConfig) {
        if (StringUtils.isNotBlank(build.getPipelineContent())) {
            try {
                return new GsonBuilder().create().fromJson(build.getPipelineContent(), LeoBuildVO.Pipeline.class);
            } catch (Exception e) {
                log.error(e.getMessage());
                return LeoBuildVO.Pipeline.builder()
                        .nodes(Lists.newArrayList(LeoBuildVO.Node.INVALID))
                        .build();
            }
        } else {
            try {
                DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(buildConfig.getBuild().getJenkins().getInstance().getUuid());
                JenkinsConfig jenkinsConfig = dsConfigHelper.build(dsConfig, JenkinsConfig.class);
                List<JenkinsPipeline.Node> nodes = blueRestDriver.getPipelineNodes(jenkinsConfig.getJenkins(), build.getBuildJobName(), String.valueOf(1));
                return LeoBuildVO.Pipeline.builder()
                        .nodes(JenkinsPipelineConverter.toLeoBuildNodes(nodes))
                        .build();
            } catch (Exception e) {
                log.error(e.getMessage());
                return LeoBuildVO.Pipeline.builder()
                        .nodes(Lists.newArrayList(LeoBuildVO.Node.QUEUE))
                        .build();
            }
        }
    }

}
