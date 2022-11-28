package com.baiyi.opscloud.leo.driver;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.driver.feign.BlueRestFeign;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/25 18:02
 * @Version 1.0
 */
@Slf4j
@Component
public class BlueRestDriver {

    private Authorization.Credential toCredential(JenkinsConfig.Jenkins config) {
        return Authorization.Credential.builder()
                .username(config.getUsername())
                .password(config.getToken())
                .build();
    }

    private BlueRestFeign buildFeign(JenkinsConfig.Jenkins config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(BlueRestFeign.class, config.getUrl());
    }

    /**
     * 查询任务构建所有节点
     * @param config
     * @param jobName
     * @param buildNumber
     * @return
     */
    public List<JenkinsPipeline.Node> getPipelineRunNodes(JenkinsConfig.Jenkins config, String jobName, String buildNumber) {
        BlueRestFeign blueRestAPI = buildFeign(config);
        return blueRestAPI.getPipelineRunNodes(toCredential(config).toBasic(), jobName, buildNumber);
    }

    /**
     * 查询步骤日志
     * @param config
     * @param jobName
     * @param buildNumber
     * @param nodeId
     * @param stepId
     * @return
     */
    public String getPipelineRunNodeStepLog(JenkinsConfig.Jenkins config, String jobName, String buildNumber, String nodeId, String stepId) {
        BlueRestFeign blueRestAPI = buildFeign(config);
        return blueRestAPI.getPipelineRunNodeStepLog(toCredential(config).toBasic(), jobName, buildNumber, nodeId, stepId);
    }

    /**
     * 查询节点步骤
     * @param config
     * @param jobName
     * @param buildNumber
     * @param nodeId
     * @return
     */
    public List<JenkinsPipeline.Step> getPipelineRunNodeSteps(JenkinsConfig.Jenkins config, String jobName, String buildNumber, String nodeId) {
        BlueRestFeign blueRestAPI = buildFeign(config);
        return blueRestAPI.getPipelineRunNodeSteps(toCredential(config).toBasic(), jobName, buildNumber, nodeId);
    }

}
