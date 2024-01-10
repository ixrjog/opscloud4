package com.baiyi.opscloud.leo.message.handler.impl.build;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.leo.response.LeoContinuousDeliveryResponse;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.jenkins.JenkinsServer;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsJobDriver;
import com.baiyi.opscloud.datasource.jenkins.model.Build;
import com.baiyi.opscloud.datasource.jenkins.model.BuildWithDetails;
import com.baiyi.opscloud.datasource.jenkins.model.JenkinsConsoleLog;
import com.baiyi.opscloud.datasource.jenkins.model.JobWithDetails;
import com.baiyi.opscloud.datasource.jenkins.server.JenkinsServerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.request.QueryLeoBuildConsoleStreamRequestParam;
import com.baiyi.opscloud.domain.param.leo.request.type.LeoRequestType;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.message.handler.base.BaseLeoContinuousDeliveryRequestHandler;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/11/29 17:33
 * @Version 1.0
 */
@Slf4j
@Component
public class QueryLeoBuildConsoleStreamRequestHandler extends BaseLeoContinuousDeliveryRequestHandler<QueryLeoBuildConsoleStreamRequestParam> {

    @Resource
    private LeoBuildService leoBuildService;

    @Resource
    private DsConfigManager dsConfigManager;

    @Resource
    private JenkinsJobDriver jenkinsJobDriver;

    @Override
    public String getMessageType() {
        return LeoRequestType.QUERY_LEO_BUILD_CONSOLE_STREAM.name();
    }

    @Override
    public void handleRequest(String sessionId, Session session, String message) throws IOException {
        QueryLeoBuildConsoleStreamRequestParam queryParam = toRequestParam(message);
        LeoBuild leoBuild = leoBuildService.getById(queryParam.getBuildId());
        if (leoBuild == null) {
            sendErrorMsgAndCloseSession(queryParam.getBuildId(), "构建信息不存在！", session);
            return;
        }
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(leoBuild);
        final String jenkinsUuid = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getJenkins)
                .map(LeoBaseModel.Jenkins::getInstance)
                .map(LeoBaseModel.DsInstance::getUuid)
                .orElseThrow(() -> new LeoBuildException("Jenkins配置不存在！"));
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(jenkinsUuid);
        JenkinsConfig jenkinsConfig = dsConfigManager.build(dsConfig, JenkinsConfig.class);

        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(jenkinsConfig.getJenkins())) {
            JobWithDetails jobWithDetails = jenkinsServer.getJob(leoBuild.getBuildJobName());
            while (true) {
                if (!session.isOpen()) {
                    break;
                }
                Build build = jobWithDetails.details().getLastBuild();
                BuildWithDetails buildWithDetails = build.details();
                if (buildWithDetails.equals(com.baiyi.opscloud.datasource.jenkins.model.Build.BUILD_HAS_NEVER_RUN)) {
                    NewTimeUtil.sleep(3L);
                } else {
                    jenkinsJobDriver.streamConsoleOutputToSession(sessionId, queryParam.getBuildId(), buildWithDetails, session, true);
                    break;
                }
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.error("Jenkins stream log error: {}", e.getMessage());
        }
    }

    private QueryLeoBuildConsoleStreamRequestParam toRequestParam(String message) {
        return toLeoRequestParam(message, QueryLeoBuildConsoleStreamRequestParam.class);
    }

    private void sendErrorMsgAndCloseSession(Integer buildId, String msg, Session session) {
        LeoContinuousDeliveryResponse<JenkinsConsoleLog.Log> response = LeoContinuousDeliveryResponse.<JenkinsConsoleLog.Log>builder()
                .body(JenkinsConsoleLog.Log.builder()
                        .buildId(buildId)
                        .log(msg)
                        .build())
                .messageType(LeoRequestType.QUERY_LEO_BUILD_CONSOLE_STREAM.name())
                .build();
        try {
            session.getBasicRemote().sendText(response.toString());
            session.close();
        } catch (IOException ignored) {
        }
    }

}