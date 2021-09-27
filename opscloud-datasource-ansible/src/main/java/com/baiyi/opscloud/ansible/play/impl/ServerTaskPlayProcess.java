package com.baiyi.opscloud.ansible.play.impl;

import com.baiyi.opscloud.ansible.play.AbstractTaskPlayProcess;
import com.baiyi.opscloud.ansible.play.PlayOutputMessage;
import com.baiyi.opscloud.ansible.play.enums.TaskMessageState;
import com.baiyi.opscloud.ansible.play.message.ServerTaskPlayMessage;
import com.baiyi.opscloud.ansible.play.task.ServerTaskPlayTask;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTaskMember;
import com.baiyi.opscloud.service.task.ServerTaskMemberService;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/9/26 5:29 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ServerTaskPlayProcess extends AbstractTaskPlayProcess<ServerTaskPlayMessage> {

    @Resource
    private ServerTaskMemberService serverTaskMemberService;

    /**
     * 播放
     *
     * @return
     */
    @Override
    public String getState() {
        return TaskMessageState.PLAY.name();
    }

    @Override
    public void process(String message, Session session) {
        ServerTaskPlayMessage playMessage = getMessage(message);
        playMessage.getServerTaskMemberIds().forEach(id -> doTask(session, id));
    }

    private void doTask(Session session, Integer serverTaskMemberId) {
        ServerTaskMember serverTaskMember = serverTaskMemberService.getById(null);
        if (serverTaskMember == null) {
            log.error("serverTaskMember不存在！ serverTaskMemberId = {}", serverTaskMemberId);
            return;
        }
        if (serverTaskMember.getFinalized()) {
            readLogFile(serverTaskMember, session);
        } else {
            // 启动线程处理会话
            Runnable run = new ServerTaskPlayTask(session, serverTaskMember);
            Thread thread = new Thread(run);
            thread.start();
        }
    }

    /**
     * 任务已经完成，一次性读取日志
     *
     * @param serverTaskMember
     * @param session
     */
    private void readLogFile(ServerTaskMember serverTaskMember, Session session) {
        String output = IOUtil.readFile(serverTaskMember.getOutputMsg());
        String error = IOUtil.readFile(serverTaskMember.getErrorMsg());
        PlayOutputMessage pom = PlayOutputMessage.builder()
                .instanceId(serverTaskMember.getServerName())
                .serverTaskId(serverTaskMember.getServerTaskId())
                .serverTaskMemberId(serverTaskMember.getId())
                .output(output)
                .error(error)
                .build();
        try {
            if (session.isOpen())
                session.getBasicRemote().sendText(pom.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ServerTaskPlayMessage getMessage(String message) {
        return new GsonBuilder().create().fromJson(message, ServerTaskPlayMessage.class);
    }

}


