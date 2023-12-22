package com.baiyi.opscloud.sshserver.listeners.event;

import com.baiyi.opscloud.sshserver.listeners.SshShellEvent;

/**
 * @Author baiyi
 * @Date 2022/8/30 15:27
 * @Version 1.0
 */
public interface ISshShellEvent {

    String getEventType();

    void handle(SshShellEvent event);

}