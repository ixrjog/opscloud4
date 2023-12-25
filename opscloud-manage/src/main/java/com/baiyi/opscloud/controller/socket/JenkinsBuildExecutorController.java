package com.baiyi.opscloud.controller.socket;

import com.baiyi.opscloud.datasource.jenkins.engine.JenkinsBuildExecutorHelper;
import com.baiyi.opscloud.datasource.jenkins.message.SimpleMessage;
import com.baiyi.opscloud.datasource.jenkins.task.WatchJenkinsBuildExecutorTask;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.leo.task.loop.LeoBuildEventLoop;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.gson.GsonBuilder;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.baiyi.opscloud.controller.socket.ServerTerminalController.WEBSOCKET_TIMEOUT;

/**
 * @Author baiyi
 * @Date 2022/8/1 13:29
 * @Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/api/ws/jenkins/build/executor/status")
@Component
public class JenkinsBuildExecutorController {

    private final String sessionId = UUID.randomUUID().toString();

    private static JenkinsBuildExecutorHelper jenkinsBuildExecutorHelper;

    private static DsInstanceService dsInstanceService;

//    private static ThreadPoolTaskExecutor leoExecutor;
//
//    @Autowired
//    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor leoExecutor) {
//        JenkinsBuildExecutorController.leoExecutor = leoExecutor;
//    }

    @Autowired
    public void setBeans(JenkinsBuildExecutorHelper jenkinsBuildExecutorHelper, DsInstanceService dsInstanceService) {
        JenkinsBuildExecutorController.jenkinsBuildExecutorHelper = jenkinsBuildExecutorHelper;
        JenkinsBuildExecutorController.dsInstanceService = dsInstanceService;
    }

    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
    }

    /**
     * 收到客户端消息后调用的方法
     * Session session
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 1024)
    public void onMessage(String message, Session session) {
        SimpleMessage simpleMessage = toMessage(message);
        DatasourceInstance instance = null;
        if (simpleMessage.getInstanceId() != null) {
            instance = dsInstanceService.getById(simpleMessage.getInstanceId());
        } else {
            if (StringUtils.isNotBlank(simpleMessage.getInstanceUuid())) {
                instance = dsInstanceService.getByUuid(simpleMessage.getInstanceUuid());
            }
        }
        if (instance == null) {
            return;
        }
        Runnable run = new WatchJenkinsBuildExecutorTask(sessionId, session, instance, jenkinsBuildExecutorHelper);
        //leoExecutor.execute(run);
        // JDK21 VirtualThreads
        Thread.ofVirtual().start(new LeoBuildEventLoop(this.sessionId, session));
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