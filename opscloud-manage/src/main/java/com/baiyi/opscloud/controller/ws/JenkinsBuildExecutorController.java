package com.baiyi.opscloud.controller.ws;

import com.baiyi.opscloud.datasource.jenkins.engine.JenkinsBuildExecutorHelper;
import com.baiyi.opscloud.datasource.jenkins.message.SimpleMessage;
import com.baiyi.opscloud.datasource.jenkins.task.WatchJenkinsBuildExecutorTask;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.baiyi.opscloud.controller.ws.ServerTerminalController.WEBSOCKET_TIMEOUT;

/**
 * @Author baiyi
 * @Date 2022/8/1 13:29
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/jenkins/build/executor/status")
@Component
public class JenkinsBuildExecutorController {

    private Session session = null;

    private final String sessionId = UUID.randomUUID().toString();

    private static JenkinsBuildExecutorHelper jenkinsBuildExecutorHelper;

    private static DsInstanceService dsInstanceService;

    @Autowired
    public void setBeans(JenkinsBuildExecutorHelper jenkinsBuildExecutorHelper, DsInstanceService dsInstanceService) {
        JenkinsBuildExecutorController.jenkinsBuildExecutorHelper = jenkinsBuildExecutorHelper;
        JenkinsBuildExecutorController.dsInstanceService = dsInstanceService;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("Jenkins Build Executor Status: 当前连接数为={}", cnt);
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
    }

    /**
     * 收到客户端消息后调用的方法
     * Session session
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 64 * 1024)
    public void onMessage(String message, Session session) {
        SimpleMessage simpleMessage = toMessage(message);
        DatasourceInstance instance = null;
        if (simpleMessage.getInstanceId() != null) {
            instance = dsInstanceService.getById(simpleMessage.getInstanceId());
        } else {
            if (StringUtils.isNotBlank(simpleMessage.getInstanceUuid()))
                instance = dsInstanceService.getByUuid(simpleMessage.getInstanceUuid());
        }
        if (instance == null) return;
        Runnable run = new WatchJenkinsBuildExecutorTask(sessionId, session, instance, jenkinsBuildExecutorHelper);
        Thread thread = new Thread(run);
        thread.start();
    }

    protected SimpleMessage toMessage(String message) {
        return new GsonBuilder().create().fromJson(message, SimpleMessage.class);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
    }

}
