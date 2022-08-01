package com.baiyi.opscloud.datasource.jenkins.task;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.jenkins.engine.JenkinsBuildExecutorHelper;
import com.baiyi.opscloud.datasource.jenkins.status.JenkinsBuildExecutorStatusVO;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2022/8/1 13:38
 * @Version 1.0
 */
@Slf4j
public class WatchJenkinsBuildExecutorTask implements Runnable {

    private final Session session;
    private final String sessionId;
    private final DatasourceInstance instance;
    private final JenkinsBuildExecutorHelper jenkinsBuildExecutorHelper;

    public WatchJenkinsBuildExecutorTask(String sessionId, Session session, DatasourceInstance instance, JenkinsBuildExecutorHelper jenkinsBuildExecutorHelper) {
        this.sessionId = sessionId;
        this.session = session;
        this.instance = instance;
        this.jenkinsBuildExecutorHelper = jenkinsBuildExecutorHelper;
    }

    @Override
    public void run() {
        while (session.isOpen()) {
            try {
                JenkinsBuildExecutorStatusVO.Children children = jenkinsBuildExecutorHelper.generatorBuildExecutorStatus(instance);
                send(children);
                TimeUnit.MILLISECONDS.sleep(10000L);
            } catch (InterruptedException e) {
            }
        }
    }

    private void send(JenkinsBuildExecutorStatusVO.Children children) {
        try {
            session.getBasicRemote().sendText(JSONUtil.writeValueAsString(children));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}