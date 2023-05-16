package com.baiyi.opscloud.datasource.jenkins.driver;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.leo.response.LeoContinuousDeliveryResponse;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.helper.BuildConsoleStreamListener;
import com.baiyi.opscloud.datasource.jenkins.model.*;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/8 14:46
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsJobDriver {

    public static final int POOLING_INTERVAL = 1;
    public static final int POOLING_TIMEOUT = 600;

    /**
     * 构建Job
     *
     * @param jenkins
     * @param jobName
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    @Retryable(retryFor = IOException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000))
    public QueueReference buildJobWithParams(JenkinsConfig.Jenkins jenkins, String jobName, Map<String, String> params) throws URISyntaxException, IOException {
        boolean crumbFlag = Optional.of(jenkins)
                .map(JenkinsConfig.Jenkins::getSecurity)
                .map(JenkinsConfig.Security::getCrumbFlag)
                .orElse(false);
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            JobWithDetails job = jenkinsServer.getJob(jobName);
            return job.build(params, crumbFlag);
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 创建Job
     *
     * @param jenkins
     * @param jobName 名称
     * @param jobXml  内容
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    @Retryable(retryFor = IOException.class, maxAttempts = 4, backoff = @Backoff(delay = 2000))
    public void createJob(JenkinsConfig.Jenkins jenkins, String jobName, String jobXml) throws URISyntaxException, IOException {
        boolean crumbFlag = Optional.of(jenkins)
                .map(JenkinsConfig.Jenkins::getSecurity)
                .map(JenkinsConfig.Security::getCrumbFlag)
                .orElse(false);
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            jenkinsServer.createJob(jobName, jobXml, crumbFlag);
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public void deleteJob(JenkinsConfig.Jenkins jenkins, String jobName) throws URISyntaxException, IOException {
        boolean crumbFlag = Optional.of(jenkins)
                .map(JenkinsConfig.Jenkins::getSecurity)
                .map(JenkinsConfig.Security::getCrumbFlag)
                .orElse(false);
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            jenkinsServer.deleteJob(jobName, crumbFlag);
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 停止正在执行的构建
     *
     * @param jenkins
     * @param jobName
     * @param buildNumber
     * @throws URISyntaxException
     * @throws IOException
     */
    @Deprecated
    public void stopJobBuild(JenkinsConfig.Jenkins jenkins, String jobName, Integer buildNumber) throws URISyntaxException, IOException {
        assert jenkins != null;
        boolean crumbFlag = Optional.of(jenkins)
                .map(JenkinsConfig.Jenkins::getSecurity)
                .map(JenkinsConfig.Security::getCrumbFlag)
                .orElse(false);
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkins)) {
            JobWithDetails jobWithDetails = jenkinsServer.getJob(jobName);
            Optional<Build> optionalBuild = jobWithDetails.getBuildByNumber(buildNumber);
            if (optionalBuild.isPresent()) {
                BuildWithDetails buildWithDetails = optionalBuild.get().details();
                if (buildWithDetails.isBuilding()) {
                    log.info("停止正在执行的构建: url={}, jobName={}, buildNumber={}", jenkins.getUrl(), jobName, buildNumber);
                    buildWithDetails.Stop(crumbFlag);
                }
            }
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 输出日志到会话
     *
     * @param sessionId
     * @param buildId
     * @param buildWithDetails
     * @param session
     * @param crumbFlag
     * @throws IOException
     * @throws InterruptedException
     */
    public void streamConsoleOutputToSession(String sessionId, int buildId, BuildWithDetails buildWithDetails, Session session, boolean crumbFlag) throws IOException, InterruptedException {
        buildWithDetails.streamConsoleOutput(new BuildConsoleStreamListener() {

            @Override
            public void onData(String newLogChunk) {
                try {
                    if (session.isOpen()) {
                        LeoContinuousDeliveryResponse<JenkinsConsoleLog.Log> response = LeoContinuousDeliveryResponse.<JenkinsConsoleLog.Log>builder()
                                .body(JenkinsConsoleLog.Log.builder()
                                        .buildId(buildId)
                                        .log(newLogChunk)
                                        .build())
                                .messageType(LeoRequestType.QUERY_LEO_BUILD_CONSOLE_STREAM.name())
                                .build();
                        session.getBasicRemote().sendText(response.toString());
                    } else {
                        finished();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }

            @Override
            public void finished() {
                // 任务日志会话关闭！
                try {
                    session.close();
                } catch (IOException ignored) {
                }
            }

        }, POOLING_INTERVAL, POOLING_TIMEOUT);
    }

}