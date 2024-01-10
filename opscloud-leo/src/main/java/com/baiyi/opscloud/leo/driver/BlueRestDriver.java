package com.baiyi.opscloud.leo.driver;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.driver.feign.BlueRestFeign;
import feign.Feign;
import feign.Request;
import feign.Response;
import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2022/11/25 18:02
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@Component
public class BlueRestDriver {

    private BlueRestFeign buildFeign(JenkinsConfig.Jenkins config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new CustomFeignErrorHandler())
                .options(new Request.Options(10L, TimeUnit.SECONDS, 60L, TimeUnit.SECONDS, false))
                .requestInterceptor(new BasicAuthRequestInterceptor(config.getUsername(),
                        config.getToken()))
                .target(BlueRestFeign.class, config.getUrl());
    }

    private BlueRestFeign buildFeignWithLog(JenkinsConfig.Jenkins config) {
        return Feign.builder()
                .options(new Request.Options(10L, TimeUnit.SECONDS, 60L, TimeUnit.SECONDS, false))
                .requestInterceptor(new BasicAuthRequestInterceptor(config.getUsername(),
                        config.getToken()))
                .target(BlueRestFeign.class, config.getUrl());
    }

    /**
     * 查询任务构建所有节点
     *
     * @param config
     * @param jobName
     * @param buildNumber
     * @return
     */
    public List<JenkinsPipeline.Node> getPipelineNodes(JenkinsConfig.Jenkins config, String jobName, String buildNumber) {
        BlueRestFeign blueRestAPI = buildFeign(config);
        return blueRestAPI.getPipelineNodes(jobName, buildNumber);
    }

    /**
     * 查询步骤日志
     *
     * @param config
     * @param jobName
     * @param buildNumber
     * @param nodeId
     * @param stepId
     * @return
     */
    public String getPipelineNodeStepLog(JenkinsConfig.Jenkins config, String jobName, String buildNumber, String nodeId, String stepId) {
        BlueRestFeign blueRestAPI = buildFeignWithLog(config);
        return blueRestAPI.getPipelineNodeStepLog(jobName, buildNumber, nodeId, stepId);
    }

    /**
     * 查询节点步骤
     *
     * @param config
     * @param jobName
     * @param buildNumber
     * @param nodeId
     * @return
     */
    public List<JenkinsPipeline.Step> getPipelineNodeSteps(JenkinsConfig.Jenkins config, String jobName, String buildNumber, String nodeId) {
        BlueRestFeign blueRestAPI = buildFeign(config);
        return blueRestAPI.getPipelineNodeSteps(jobName, buildNumber, nodeId);
    }

    public JenkinsPipeline.Step stopPipeline(JenkinsConfig.Jenkins config, String jobName, String buildNumber) {
        BlueRestFeign blueRestAPI = buildFeign(config);
        return blueRestAPI.stopPipeline(jobName, buildNumber);
    }

    private class CustomFeignErrorHandler implements ErrorDecoder {

        @SneakyThrows
        @Override
        public Exception decode(String s, Response response) {
            log.debug("decode error: {}", s);
            throw new Exception("decode error");
        }
    }

}